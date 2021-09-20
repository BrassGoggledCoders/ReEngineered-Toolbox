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
            WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();

            for (Variant variant : this.getVariants()) {
                IBakedModel ibakedmodel = pModelBakery.getBakedModel(variant.getModelLocation(), pTransform, pSpriteGetter);
                weightedbakedmodel$builder.add(ibakedmodel, variant.getWeight());
            }

            return weightedbakedmodel$builder.build();
        }
    }

    public static class Deserializer implements JsonDeserializer<VariantList> {
        public VariantList deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            List<Variant> list = Lists.newArrayList();
            if (p_deserialize_1_.isJsonArray()) {
                JsonArray jsonarray = p_deserialize_1_.getAsJsonArray();
                if (jsonarray.size() == 0) {
                    throw new JsonParseException("Empty variant array");
                }

                for(JsonElement jsonelement : jsonarray) {
                    list.add(p_deserialize_3_.deserialize(jsonelement, Variant.class));
                }
            } else {
                list.add(p_deserialize_3_.deserialize(p_deserialize_1_, Variant.class));
            }

            return new TransformedVariantList(list);
        }
    }
}
