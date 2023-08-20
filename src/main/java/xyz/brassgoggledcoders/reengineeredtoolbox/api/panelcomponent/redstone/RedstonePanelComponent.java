package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public class RedstonePanelComponent extends PanelComponent implements IRedstonePanelComponent {
    private final boolean canConnect;

    public RedstonePanelComponent() {
        this(true);
    }

    public RedstonePanelComponent(boolean canConnect) {
        this.canConnect = canConnect;
    }

    @Override
    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState, IPanelPosition panelPosition) {
        return canConnect;
    }

    @Override
    public int getSignal(IFrameEntity frameEntity, PanelState panelState, IPanelPosition panelPosition) {
        return 0;
    }
}
