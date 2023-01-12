package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeContainer;

public class FreezerPanelEntity extends MachinePanelEntity<FreezerRecipe, FreezerRecipeContainer> implements MenuProvider {
    public FreezerPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    public FreezerPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.FREEZER.getPanelEntityType(), frameEntity, panelState);
    }

    @Override
    public RecipeType<FreezerRecipe> getRecipeType() {
        return null;
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getPanel().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return null;
    }
}
