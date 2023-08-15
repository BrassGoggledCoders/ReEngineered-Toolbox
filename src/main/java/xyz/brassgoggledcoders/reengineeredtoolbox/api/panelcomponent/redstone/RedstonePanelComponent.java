package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public class RedstonePanelComponent extends PanelComponent {
    private final boolean canConnect;

    public RedstonePanelComponent() {
        this(true);
    }

    public RedstonePanelComponent(boolean canConnect) {
        this.canConnect = canConnect;
    }

    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState) {
        return canConnect;
    }

    public int getSignal(PanelState panelState, IFrameEntity frameEntity, IPanelPosition panelPosition) {
        return 0;
    }
}
