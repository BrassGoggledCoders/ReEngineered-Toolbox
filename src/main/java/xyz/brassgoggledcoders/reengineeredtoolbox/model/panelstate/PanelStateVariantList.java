package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IBakedPanelModel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModel;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PanelStateVariantList implements IPanelModel {
    private final List<Variant> variants;

    public PanelStateVariantList(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    @Override
    public IBakedPanelModel bake() {
        return null;
    }

    @Override
    @Nonnull
    public Collection<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> pModelGetter, Set<Pair<String, String>> pMissingTextureErrors) {
        return this.getVariants()
                .stream()
                .map(Variant::getModelLocation)
                .distinct()
                .flatMap((modelLocation) -> pModelGetter.apply(modelLocation)
                        .getMaterials(pModelGetter, pMissingTextureErrors)
                        .stream()
                )
                .collect(Collectors.toSet());
    }

    public static class Deserializer implements JsonDeserializer<PanelStateVariantList> {
        public PanelStateVariantList deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Variant> list = Lists.newArrayList();
            if (jsonElement.isJsonArray()) {
                JsonArray variantArray = jsonElement.getAsJsonArray();
                if (variantArray.size() == 0) {
                    throw new JsonParseException("Empty variant array");
                }

                for(JsonElement variantElement : variantArray) {
                    list.add(context.deserialize(variantElement, Variant.class));
                }
            } else {
                list.add(context.deserialize(jsonElement, Variant.class));
            }

            return new PanelStateVariantList(list);
        }
    }
}
