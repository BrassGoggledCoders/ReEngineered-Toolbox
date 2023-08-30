package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.Consumer;

public record PanelInfo(
        PanelState panelState,
        PanelEntity panelEntity
) {
    public void ifEntityPresent(Consumer<PanelEntity> panelEntityConsumer) {
        if (this.panelEntity() != null) {
            panelEntityConsumer.accept(this.panelEntity());
        }
    }
}
