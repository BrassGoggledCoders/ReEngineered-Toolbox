package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.Consumer;
import java.util.function.Function;


public interface IFrameEntity extends ICapabilityProvider {
    default InteractionResultHolder<PanelState> putPanelState(@NotNull Direction direction, @Nullable PanelState panelState) {
        return putPanelState(direction, panelState, false);
    }

    InteractionResultHolder<PanelState> putPanelState(@NotNull Direction direction, @Nullable PanelState panelState, boolean replace);

    @SuppressWarnings("UnusedReturnValue")
    default InteractionResultHolder<PanelState> updatePanelState(@NotNull Direction direction, Function<PanelState, PanelState> update) {
        PanelState panelState = this.getPanelState(direction);
        PanelState newState = update.apply(panelState);
        if (panelState != newState) {
            return putPanelState(direction, newState, true);
        } else {
            return InteractionResultHolder.fail(panelState);
        }
    }

    @NotNull
    PanelState getPanelState(@NotNull Direction direction);

    @Nullable
    PanelEntity getPanelEntity(@Nullable Direction direction);

    @NotNull
    BlockPos getFramePos();

    @NotNull
    Level getFrameLevel();

    default void openMenu(Player player, PanelEntity panelEntity, MenuProvider menuProvider) {
        this.openMenu(player, panelEntity, menuProvider, friendlyByteBuf -> {
        });
    }

    void openMenu(Player player, PanelEntity panelEntity, MenuProvider menuProvider, @NotNull Consumer<FriendlyByteBuf> friendlyByteBuf);

    boolean isValid();

    void scheduleTick(@NotNull Direction direction, Panel panel, int ticks);

    boolean changeFrameSlot(@NotNull BlockHitResult hitResult, ItemStack toolStack);

    <T> void notifyStorageChange(Capability<T> frequencyItemHandler);
}
