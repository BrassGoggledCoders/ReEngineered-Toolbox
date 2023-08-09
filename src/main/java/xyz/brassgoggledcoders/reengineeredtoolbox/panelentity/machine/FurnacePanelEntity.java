package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.furnace.IRETCookingRecipe;

public class FurnacePanelEntity extends MachinePanelEntity<IRETCookingRecipe, RecipeWrapper> {
    public FurnacePanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    protected FrameSlot createEnergyFrameSlot() {
        return null;
    }

    @Override
    protected RecipeType<IRETCookingRecipe> getRecipeType() {
        return null;
    }

    @Override
    protected RecipeWrapper getRecipeContainer() {
        return null;
    }

    @Override
    protected boolean handleRecipe(IRETCookingRecipe recipe) {
        return false;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }
}
