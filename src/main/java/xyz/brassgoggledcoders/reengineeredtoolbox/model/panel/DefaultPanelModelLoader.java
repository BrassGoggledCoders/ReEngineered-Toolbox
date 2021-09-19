package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModelLoader;

public class DefaultPanelModelLoader implements IPanelModelLoader<PanelModel> {
    public static final ResourceLocation ID = ReEngineeredToolbox.rl("default");

    @Override
    public PanelModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return null;
    }
}
