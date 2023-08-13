package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import java.util.Objects;

public abstract class PanelComponent {
    private Panel panel;

    public void setPanel(@NotNull Panel panel) {
        this.panel = panel;
    }

    public Panel getPanel() {
        return Objects.requireNonNull(this.panel, "Tried to get panel too early");
    }
}
