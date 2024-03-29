package xyz.brassgoggledcoders.reengineeredtoolbox.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class FrameMenuProvider implements MenuProvider {
    private final MenuProvider panelMenuProvider;

    public FrameMenuProvider(MenuProvider panelMenuProvider) {
        this.panelMenuProvider = panelMenuProvider;
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return panelMenuProvider.getDisplayName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return panelMenuProvider.createMenu(pContainerId, pPlayerInventory, pPlayer);
    }
}
