package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

public interface IPlacementPanelComponent {
    @Nullable
    PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame, @Nullable PanelState current);
}
