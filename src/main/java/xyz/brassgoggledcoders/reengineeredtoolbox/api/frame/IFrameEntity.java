package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelMenuProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;

import java.util.function.Consumer;


public interface IFrameEntity {
    default InteractionResultHolder<PanelState> putPanelState(@NotNull Direction direction, @Nullable PanelState panelState) {
        return putPanelState(direction, panelState, false);
    }

    InteractionResultHolder<PanelState> putPanelState(@NotNull Direction direction, @Nullable PanelState panelState, boolean replace);

    @NotNull
    PanelState getPanelState(@NotNull Direction direction);

    @Nullable
    PanelEntity getPanelEntity(@NotNull Direction direction);

    @NotNull
    BlockPos getFramePos();

    @NotNull
    Level getFrameLevel();

    ITypedSlotHolder getTypedSlotHolder();

    default void openMenu(Player player, PanelMenuProvider menuProvider) {
        this.openMenu(player, menuProvider, friendlyByteBuf -> {
        });
    }

    void openMenu(Player player, PanelMenuProvider menuProvider, @NotNull Consumer<FriendlyByteBuf> friendlyByteBuf);

    boolean isValid();
}
