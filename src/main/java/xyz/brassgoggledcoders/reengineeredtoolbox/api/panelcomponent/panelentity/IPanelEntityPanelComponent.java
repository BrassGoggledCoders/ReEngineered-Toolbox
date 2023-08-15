package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.panelentity;

import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public interface IPanelEntityPanelComponent {
    @Nullable
    PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState);
}
