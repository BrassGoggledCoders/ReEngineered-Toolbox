package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public class PlacementPanelComponent extends PanelComponent {
    @Nullable
    public PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame) {
        return this.getPanel().defaultPanelState();
    }
}
