package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyBackedFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item.FrequencyBackedItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredMenus;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FreezerMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeContainer;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;

import javax.annotation.ParametersAreNonnullByDefault;

public class FreezerPanelEntity extends MachinePanelEntity<FreezerRecipe, FreezerRecipeContainer> implements MenuProvider {

    private final FrameSlot fluidIn;
    private final FrameSlot itemIn;
    private final FrameSlot itemOut;
    private final FrequencyBackedItemHandler itemHandler;
    private final FrequencyBackedFluidHandler fluidHandler;

    public FreezerPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.fluidIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.FLUID_SLOT_IN, FrameSlotViews.TOP_RIGHT_4X4));
        this.itemIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ITEM_SLOT_IN, FrameSlotViews.BOTTOM_LEFT_4X4));
        this.itemOut = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ITEM_SLOT_OUT, FrameSlotViews.BOTTOM_RIGHT_4X4));

        this.fluidHandler = new FrequencyBackedFluidHandler(fluidIn, IOStyle.ONLY_EXTRACT, frameEntity);
        this.itemHandler = new FrequencyBackedItemHandler(new FrameSlot[]{itemIn, itemOut}, IOStyle.BOTH, frameEntity);
    }

    @Override
    protected FrameSlot createEnergyFrameSlot() {
        return new FrameSlot(ReEngineeredText.ENERGY_SLOT_IN, FrameSlotViews.TOP_LEFT_4X4);
    }

    @Override
    protected RecipeType<FreezerRecipe> getRecipeType() {
        return ReEngineeredRecipes.FREEZER_TYPE.get();
    }

    @Override
    protected FreezerRecipeContainer getRecipeContainer() {
        return new FreezerRecipeContainer(this.itemHandler, this.fluidHandler);
    }

    public FreezerPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this(ReEngineeredPanels.FREEZER.getPanelEntityType(), frameEntity, panelState);
    }

    @Override
    protected boolean handleRecipe(FreezerRecipe freezerRecipe) {
        if (this.getEnergyHandler().extractEnergy(freezerRecipe.power(), false) >= freezerRecipe.power()) {
            if (this.itemHandler.insertItem(1, freezerRecipe.getResultItem(), true).isEmpty()) {
                this.incrementProgress();
                if (this.getProgress() >= freezerRecipe.time()) {
                    this.itemHandler.extractItem(0, 1, false);
                    this.fluidHandler.drain(freezerRecipe.fluidInput().getAmount(), FluidAction.EXECUTE);
                    this.itemHandler.insertItem(1, freezerRecipe.assemble(this.getRecipeContainer()), false);
                    this.setProgress(0);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fluidIn.deserializeNBT(pTag.getCompound("FluidIn"));
        this.itemIn.deserializeNBT(pTag.getCompound("ItemIn"));
        this.itemOut.deserializeNBT(pTag.getCompound("ItemOut"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("FluidIn", this.fluidIn.serializeNBT());
        pTag.put("ItemIn", this.itemIn.serializeNBT());
        pTag.put("ItemOut", this.itemOut.serializeNBT());
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
                this.getPanelPosition(),
                this.itemHandler,
                this.fluidHandler::getTankView,
                () -> this.getCachedRecipe().getRecipe()
                        .map(freezerRecipe -> new ProgressView(
                                this.getProgress(),
                                freezerRecipe.time()
                        ))
                        .orElse(ProgressView.NULL),
                this.getEnergyHandler()::getView
        );
    }
}
