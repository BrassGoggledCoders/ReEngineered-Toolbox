package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class FrameUnbakedGeometry implements IUnbakedGeometry<FrameUnbakedGeometry> {
    private final UnbakedModel frameModel;

    public FrameUnbakedGeometry(UnbakedModel frameModel) {
        this.frameModel = frameModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new FrameBakedModel(
                this.frameModel.bake(bakery, spriteGetter, modelState, modelLocation)
        );
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.frameModel.getMaterials(modelGetter, missingTextureErrors);
    }
}
