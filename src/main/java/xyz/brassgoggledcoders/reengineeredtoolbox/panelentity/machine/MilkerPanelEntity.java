package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyBackedFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb.FluidOrbParticleProcess;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.RecipeCache;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking.MilkingContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking.MilkingRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.saveddata.MilkingSavedData;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.util.VectorHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class MilkerPanelEntity extends PanelEntity {
    private final FrameSlot redstoneIn;
    private final FrameSlot energyIn;
    private final FrameSlot fluidOut;

    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;
    private final FrequencyBackedEnergyHandler energyHandler;
    private final FrequencyBackedFluidHandler fluidHandler;

    private final RecipeCache<MilkingRecipe, MilkingContainer> recipeCache;

    private final List<FluidOrbParticleProcess> particleProcessList;
    private long nextAllowedActivation;
    private boolean powered;

    public MilkerPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);
        this.redstoneIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN, FrameSlotViews.TOP_RIGHT_4X4));
        this.energyIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ENERGY_SLOT_IN, FrameSlotViews.TOP_LEFT_4X4));
        this.fluidOut = this.registerFrameSlot(new FrameSlot(ReEngineeredText.FLUID_SLOT_OUT, FrameSlotViews.BOTTOM_CENTERED_4X4));

        this.energyHandler = new FrequencyBackedEnergyHandler(energyIn, IOStyle.ONLY_EXTRACT, frameEntity);
        this.fluidHandler = new FrequencyBackedFluidHandler(fluidOut, IOStyle.ONLY_INSERT, frameEntity);

        this.redstoneHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);

        this.recipeCache = new RecipeCache<>(ReEngineeredRecipes.MILKING_TYPE.get(), 4, false);
        this.particleProcessList = new ArrayList<>();

        this.nextAllowedActivation = 0;
        this.powered = false;
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.redstoneHandlerLazyOptional.map(redstoneHandler -> redstoneHandler.getPower(redstoneIn.getFrequency()))
                    .ifPresent(this::setPowerAndUpdate);
        }
    }

    private void setPowerAndUpdate(int power) {
        if (power > 0 != powered) {
            this.powered = power > 0;
            if (power > 0 && this.getLevel().getGameTime() > nextAllowedActivation) {
                this.getFrameEntity()
                        .scheduleTick(this.getPanelPosition(), this.getPanel(), 4);
            }
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (!this.particleProcessList.isEmpty()) {
            Iterator<FluidOrbParticleProcess> processIterator = this.particleProcessList.iterator();
            while (processIterator.hasNext()) {
                FluidOrbParticleProcess particleProcess = processIterator.next();
                if (particleProcess.isValid()) {
                    particleProcess.spawnParticles();
                } else {
                    processIterator.remove();
                }
            }
        }
    }

    @Override
    public void scheduledTick() {
        super.scheduledTick();
        if (this.energyHandler.extractEnergy(1000, true) == 1000) {
            this.nextAllowedActivation = this.getLevel().getGameTime() + 200;
            targetEntitiesForMilking();
        }
    }

    private void targetEntitiesForMilking() {
        AABB aabb = this.getAABBForMilking(this.getPanelPosition());
        Optional<MilkingSavedData> milkingSavedDataOpt = MilkingSavedData.getFor(this.getLevel());
        if (aabb != null && milkingSavedDataOpt.isPresent()) {
            Iterator<Entity> entityIterator = this.getLevel().getEntities((Entity) null, aabb, entity -> !entity.isSpectator() && !(entity instanceof ItemEntity))
                    .iterator();
            MilkingSavedData milkingSavedData = milkingSavedDataOpt.get();
            long gameTime = this.getLevel().getGameTime();
            while (entityIterator.hasNext() && this.energyHandler.getEnergyStored() > 1000 && this.fluidHandler.hasCapacity()) {
                Entity entity = entityIterator.next();
                if (milkingSavedData.isMilkingAllowed(entity.getUUID(), gameTime)) {
                    if (!attemptMilking(entity)) {
                        milkingSavedData.setNextMilkingAllowed(
                                entity.getUUID(),
                                gameTime + (20 * 30)
                        );
                    }
                }
            }
        }
    }

    private boolean attemptMilking(Entity entity) {
        MilkingContainer milkingContainer = new MilkingContainer(entity);
        return this.recipeCache.getRecipe(milkingContainer, this.getLevel())
                .map(milkingRecipe -> {
                    FluidStack fluidStack = milkingRecipe.assembleFluid(milkingContainer);
                    if (this.fluidHandler.isFluidValid(0, fluidStack)) {
                        if (this.fluidHandler.fill(fluidStack, FluidAction.EXECUTE) > 0) {
                            this.energyHandler.extractEnergy(1000, false);
                            MilkingSavedData.getFor(this.getLevel())
                                    .ifPresent(milkingSavedData -> milkingSavedData.setNextMilkingAllowed(
                                            entity.getUUID(),
                                            this.getLevel().getGameTime() + milkingRecipe.getCoolDown()
                                    ));
                            this.particleProcessList.add(new FluidOrbParticleProcess(
                                    entity,
                                    this.getLevel().getGameTime() + (milkingRecipe.getCoolDown() / 2),
                                    VectorHelper.centered(this.getPanelPosition().offset(this.getFrameEntity())),
                                    fluidStack.getFluid()
                            ));
                        }
                    }

                    return true;
                })
                .orElse(false);
    }

    @Nullable
    private AABB getAABBForMilking(@NotNull IPanelPosition panelPosition) {
        Direction facing = panelPosition.getFacing();

        AABB aabb = null;
        if (facing != null) {
            BlockPos offset = panelPosition.offset(this.getFrameEntity());
            aabb = new AABB(offset);
            aabb = aabb.move(facing.getStepX() * 2, facing.getStepY(), facing.getStepZ() * 2);
            aabb = aabb.inflate(2, 1, 2);
        }

        return aabb;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fluidOut.deserializeNBT(pTag.getCompound("FluidOut"));
        this.redstoneIn.deserializeNBT(pTag.getCompound("RedstoneIn"));
        this.energyIn.deserializeNBT(pTag.getCompound("EnergyIn"));
        this.nextAllowedActivation = pTag.getLong("NextAllowedActivation");
        this.powered = pTag.getBoolean("Powered");
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("FluidOut", this.fluidOut.serializeNBT());
        pTag.put("RedstoneIn", this.redstoneIn.serializeNBT());
        pTag.put("EnergyIn", this.energyIn.serializeNBT());
        pTag.putLong("NextAllowedActivation", this.nextAllowedActivation);
        pTag.putBoolean("Powered", this.powered);
    }
}
