package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.core.BlockPos;
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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyCapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;


public interface IFrameEntity extends ICapabilityProvider {
    default InteractionResultHolder<PanelState> putPanelState(@NotNull IPanelPosition panelPosition, @Nullable PanelState panelState) {
        return putPanelState(panelPosition, panelState, false);
    }

    InteractionResultHolder<PanelState> putPanelState(@NotNull IPanelPosition panelPosition, @Nullable PanelState panelState, boolean replace);

    @SuppressWarnings("UnusedReturnValue")
    default InteractionResultHolder<PanelState> updatePanelState(@NotNull IPanelPosition panelPosition, Function<PanelState, PanelState> update) {
        PanelState panelState = this.getPanelState(panelPosition);
        PanelState newState = update.apply(panelState);
        if (panelState != newState) {
            return putPanelState(panelPosition, newState, true);
        } else {
            return InteractionResultHolder.fail(panelState);
        }
    }

    boolean hasPanel(@NotNull IPanelPosition panelPosition);

    /**
     * @param panelPosition the position of the panel
     * @param player        the player attempting to remove the panel
     * @param heldItem      the tool used to remove the panel
     * @return on the server, a list of Items that should be dropped when removing the Panel, otherwise empty
     */
    List<ItemStack> removePanel(@NotNull IPanelPosition panelPosition, @Nullable Player player, @NotNull ItemStack heldItem);

    @NotNull
    PanelState getPanelState(@NotNull IPanelPosition panelPosition);

    @Nullable
    PanelEntity getPanelEntity(@Nullable IPanelPosition panelPosition);

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

    void scheduleTick(@NotNull IPanelPosition panelPosition, Panel panel, int ticks);

    boolean changeFrameSlot(@NotNull BlockHitResult hitResult, ItemStack toolStack);

    <T> void notifyStorageChange(Capability<T> frequencyItemHandler);

    IFrequencyCapabilityProvider getFrequencyProvider();

    Map<IPanelPosition, PanelInfo> getPanelInfo();

    void needsSaved();
}
