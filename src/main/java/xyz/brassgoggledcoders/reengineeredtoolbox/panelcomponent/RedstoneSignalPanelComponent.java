package xyz.brassgoggledcoders.reengineeredtoolbox.panelcomponent;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone.RedstonePanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneIOPanelEntity;

public class RedstoneSignalPanelComponent extends RedstonePanelComponent {
    @Override
    public int getSignal(IFrameEntity frameEntity, PanelState panelState, IPanelPosition panelPosition) {
        if (frameEntity.getPanelEntity(panelPosition) instanceof RedstoneIOPanelEntity redstoneIOPanel) {
            return redstoneIOPanel.getSignal();
        }

        return super.getSignal(frameEntity, panelState, panelPosition);
    }
}
