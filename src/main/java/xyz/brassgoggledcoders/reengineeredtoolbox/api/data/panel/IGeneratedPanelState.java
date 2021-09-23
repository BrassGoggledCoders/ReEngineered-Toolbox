package xyz.brassgoggledcoders.reengineeredtoolbox.api.data.panel;

import com.google.gson.JsonElement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import java.util.function.Supplier;

public interface IGeneratedPanelState extends Supplier<JsonElement> {
    Panel getOwner();
}
