package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.util.GsonHelper;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class PanelStateDefinition {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(PanelStateDefinition.class, new PanelStateDefinition.Deserializer())
            .registerTypeAdapter(Variant.class, new Variant.Deserializer())
            .registerTypeAdapter(MultiVariant.class, new PanelMultiVariant.PanelDeserializer())
            .create();
    private final Map<String, MultiVariant> variants = Maps.newLinkedHashMap();

    public PanelStateDefinition(Map<String, MultiVariant> pVariants) {
        this.variants.putAll(pVariants);
    }

    public Map<String, MultiVariant> getVariants() {
        return variants;
    }

    public MultiVariant getVariant(String name) {
        return variants.get(name);
    }

    public static PanelStateDefinition fromStream(Reader pReader) {
        return GsonHelper.fromJson(GSON, pReader, PanelStateDefinition.class);
    }

    public static class Deserializer implements JsonDeserializer<PanelStateDefinition> {
        public PanelStateDefinition deserialize(JsonElement pJson, Type pType, JsonDeserializationContext pContext) throws JsonParseException {
            JsonObject jsonobject = pJson.getAsJsonObject();
            Map<String, MultiVariant> map = this.getVariants(pContext, jsonobject);
            if (!map.isEmpty()) {
                return new PanelStateDefinition(map);
            } else {
                throw new JsonParseException("'variants' not found");
            }
        }

        protected Map<String, MultiVariant> getVariants(JsonDeserializationContext pContext, JsonObject pJson) {
            Map<String, MultiVariant> map = Maps.newHashMap();
            if (pJson.has("variants")) {
                JsonObject jsonobject = GsonHelper.getAsJsonObject(pJson, "variants");

                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), pContext.deserialize(entry.getValue(), MultiVariant.class));
                }
            }

            return map;
        }
    }
}
