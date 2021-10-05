package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

public class TransformedVariantList extends VariantList {
    public TransformedVariantList(List<Variant> variants) {
        super(variants);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public IBakedModel bake(ModelBakery pModelBakery, Function<RenderMaterial, TextureAtlasSprite> pSpriteGetter, IModelTransform pTransform, ResourceLocation pLocation) {
        if (this.getVariants().isEmpty()) {
            return null;
        } else {
            WeightedBakedModel.Builder weightedBakedModelBuilder = new WeightedBakedModel.Builder();

            for (Variant variant : this.getVariants()) {
                IBakedModel bakedModel = pModelBakery.getBakedModel(variant.getModelLocation(), pTransform, pSpriteGetter);
                weightedBakedModelBuilder.add(bakedModel, variant.getWeight());
            }

            return weightedBakedModelBuilder.build();
        }
    }

    public static class Deserializer implements JsonDeserializer<VariantList> {
        public VariantList deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            List<Variant> list = Lists.newArrayList();
            if (element.isJsonArray()) {
                JsonArray jsonArray = element.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    throw new JsonParseException("Empty variant array");
                }

                for (JsonElement arrayElement : jsonArray) {
                    list.add(context.deserialize(arrayElement, Variant.class));
                }
            } else {
                list.add(context.deserialize(element, Variant.class));
            }

            return new TransformedVariantList(list);
        }
    }
}
