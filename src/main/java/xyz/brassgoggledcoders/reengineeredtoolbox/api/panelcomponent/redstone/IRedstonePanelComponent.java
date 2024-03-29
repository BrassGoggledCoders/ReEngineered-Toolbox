package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

public interface IRedstonePanelComponent {
    boolean canConnectRedstone(IFrameEntity entity, PanelState panelState, IPanelPosition panelPosition);

    int getSignal(IFrameEntity frameEntity, PanelState panelState, IPanelPosition panelPosition);
}
