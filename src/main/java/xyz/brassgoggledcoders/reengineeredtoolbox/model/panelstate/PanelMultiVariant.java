package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.generators.ModelFile;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

public class PanelMultiVariant extends MultiVariant {

    public PanelMultiVariant(List<Variant> pVariants) {
        super(pVariants);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public BakedModel bake(ModelBakery pModelBakery, Function<Material, TextureAtlasSprite> pSpriteGetter, ModelState pTransform, ResourceLocation pLocation) {
        if (pTransform.getRotation().isIdentity()) {
            return super.bake(pModelBakery, pSpriteGetter, pTransform, pLocation);
        } else if (this.getVariants().isEmpty()) {
            return null;
        } else {
            WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();

            for (Variant variant : this.getVariants()) {
                ModelState modelState = new SimpleModelState(pTransform.getRotation().compose(variant.getRotation()));
                BakedModel bakedmodel = pModelBakery.bake(variant.getModelLocation(), modelState, pSpriteGetter);
                weightedbakedmodel$builder.add(bakedmodel, variant.getWeight());
            }

            return weightedbakedmodel$builder.build();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class PanelDeserializer implements JsonDeserializer<MultiVariant> {
        public MultiVariant deserialize(JsonElement pJson, Type pType, JsonDeserializationContext pContext) throws JsonParseException {
            List<Variant> list = Lists.newArrayList();
            if (pJson.isJsonArray()) {
                JsonArray jsonarray = pJson.getAsJsonArray();
                if (jsonarray.isEmpty()) {
                    throw new JsonParseException("Empty variant array");
                }

                for (JsonElement jsonelement : jsonarray) {
                    list.add(pContext.deserialize(jsonelement, Variant.class));
                }
            } else {
                list.add(pContext.deserialize(pJson, Variant.class));
            }

            return new PanelMultiVariant(list);
        }
    }
}
