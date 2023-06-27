package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyBackedFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item.FrequencyBackedItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredMenus;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FreezerMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.CachedRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;

import javax.annotation.ParametersAreNonnullByDefault;

public class FreezerPanelEntity extends PanelEntity implements MenuProvider {

    private final FrameSlot energyIn;
    private final FrameSlot fluidIn;
    private final FrameSlot itemIn;
    private final FrameSlot itemOut;
    private final CachedRecipe<FreezerRecipe, FreezerRecipeContainer> cachedRecipe;
    private final FrequencyBackedItemHandler itemHandler;
    private final FrequencyBackedFluidHandler fluidHandler;
    private final FrequencyBackedEnergyHandler energyHandler;
    private final FreezerRecipeContainer recipeContainer;

    private int progress;
    private int coolDown;

    public FreezerPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.energyIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ENERGY_SLOT_IN, FrameSlotViews.TOP_LEFT_4X4));
        this.fluidIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.FLUID_SLOT_IN, FrameSlotViews.TOP_RIGHT_4X4));
        this.itemIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ITEM_SLOT_IN, FrameSlotViews.BOTTOM_LEFT_4X4));
        this.itemOut = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ITEM_SLOT_OUT, FrameSlotViews.BOTTOM_RIGHT_4X4));

        this.cachedRecipe = new CachedRecipe<>(ReEngineeredRecipes.FREEZER_TYPE.get());

        this.fluidHandler = new FrequencyBackedFluidHandler(fluidIn, IOStyle.ONLY_EXTRACT, frameEntity);
        this.itemHandler = new FrequencyBackedItemHandler(new FrameSlot[]{itemIn, itemOut}, IOStyle.BOTH, frameEntity);
        this.energyHandler = new FrequencyBackedEnergyHandler(energyIn, IOStyle.ONLY_EXTRACT, frameEntity);

        this.recipeContainer = new FreezerRecipeContainer(this.itemHandler, this.fluidHandler);
    }

    public FreezerPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this(ReEngineeredPanels.FREEZER.getPanelEntityType(), frameEntity, panelState);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (coolDown <= 0 && this.energyHandler.getEnergyStored() > 0) {
            Option<FreezerRecipe> recipe = this.cachedRecipe.getRecipe(this.getLevel(), this.recipeContainer);

            if (!recipe.exists(this::handleFreezeRecipe)) {
                this.coolDown = 20;
                this.progress = 0;
            }
        } else {
            coolDown--;
        }
    }

    private boolean handleFreezeRecipe(FreezerRecipe freezerRecipe) {
        if (this.energyHandler.extractEnergy(freezerRecipe.power(), false) >= freezerRecipe.power()) {
            if (this.itemHandler.insertItem(1, freezerRecipe.getResultItem(), true).isEmpty()) {
                this.progress++;
                if (this.progress >= freezerRecipe.time()) {
                    this.itemHandler.extractItem(0, 1, false);
                    this.fluidHandler.drain(freezerRecipe.fluidInput().getAmount(), FluidAction.EXECUTE);
                    this.itemHandler.insertItem(1, freezerRecipe.assemble(this.recipeContainer), false);
                    this.progress = 0;
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.progress = pTag.getInt("Progress");
        this.energyIn.deserializeNBT(pTag.getCompound("EnergyIn"));
        this.fluidIn.deserializeNBT(pTag.getCompound("FluidIn"));
        this.itemIn.deserializeNBT(pTag.getCompound("ItemIn"));
        this.itemOut.deserializeNBT(pTag.getCompound("ItemOut"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.putInt("Progress", this.progress);
        pTag.put("EnergyIn", this.energyIn.serializeNBT());
        pTag.put("FluidIn", this.fluidIn.serializeNBT());
        pTag.put("ItemIn", this.itemIn.serializeNBT());
        pTag.put("ItemOut", this.itemOut.serializeNBT());
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getPanel().getName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FreezerMenu(
                ReEngineeredMenus.FREEZER.get(),
                pContainerId,
                pPlayerInventory,
                ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()),
                this.getPanel(),
                this.getFacing(),
                this.itemHandler,
                this.fluidHandler::getTankView,
                () -> this.cachedRecipe.getRecipe()
                        .map(freezerRecipe -> new ProgressView(
                                this.progress,
                                freezerRecipe.time()
                        ))
                        .orElse(ProgressView.NULL),
                this.energyHandler::getView
        );
    }
}
