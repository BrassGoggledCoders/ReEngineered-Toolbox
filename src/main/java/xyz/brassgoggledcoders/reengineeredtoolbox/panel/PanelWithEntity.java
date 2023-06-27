package xyz.brassgoggledcoders.reengineeredtoolbox.panel;

import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.BiFunction;

public class PanelWithEntity extends Panel {
    private final BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor;

    public PanelWithEntity(BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}
