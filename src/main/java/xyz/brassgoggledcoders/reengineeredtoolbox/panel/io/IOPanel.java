package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;

import java.util.function.BiFunction;

public class IOPanel extends Panel {
    private final BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity> panelEntityConstructor;

    public IOPanel(BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}
