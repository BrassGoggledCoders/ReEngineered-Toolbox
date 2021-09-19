package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class PanelStateDefinition {
    private final Map<String, PanelStateVariantList> variants;

    public PanelStateDefinition(Map<String, PanelStateVariantList> variants) {
        this.variants = variants;
    }

    public Map<String, PanelStateVariantList> getVariants() {
        return variants;
    }

    public static class Deserializer implements JsonDeserializer<PanelStateDefinition> {
        @Override
        public PanelStateDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Map<String, PanelStateVariantList> map = Maps.newHashMap();
            if (jsonObject.has("variants")) {
                JsonObject jsonobject = JSONUtils.getAsJsonObject(jsonObject, "variants");

                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), context.deserialize(entry.getValue(), PanelStateVariantList.class));
                }

            } else {
                throw new JsonParseException("Missing field 'variants'");
            }
            if (map.isEmpty()) {
                throw new JsonParseException("Field 'variants' contained no elements");
            }
            return new PanelStateDefinition(map);
        }
    }
}
