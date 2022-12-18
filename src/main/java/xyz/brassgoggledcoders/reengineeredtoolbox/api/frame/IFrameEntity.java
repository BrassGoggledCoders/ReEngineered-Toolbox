package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public interface IFrameEntity {
    InteractionResultHolder<PanelState> setPanelState(@NotNull Direction direction, @Nullable PanelState panelState);

    @NotNull
    PanelState getPanelState(@NotNull Direction direction);

    @Nullable
    PanelEntity getPanelEntity(@NotNull Direction direction);
}
