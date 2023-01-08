package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;

import java.util.List;

public interface IPanelMenu {

    @NotNull
    List<Port> getPorts();
}
