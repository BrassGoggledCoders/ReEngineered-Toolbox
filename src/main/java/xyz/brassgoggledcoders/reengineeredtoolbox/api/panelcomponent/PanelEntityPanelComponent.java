package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent;

import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.BiFunction;

public class PanelEntityPanelComponent extends PanelComponent {

    private final BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor;

    public PanelEntityPanelComponent(BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}
