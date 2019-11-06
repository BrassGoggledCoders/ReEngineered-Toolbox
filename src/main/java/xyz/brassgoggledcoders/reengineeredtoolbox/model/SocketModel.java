package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class SocketModel implements IUnbakedModel {
    private final IModel socketModel;
    private IBakedModel socketFrameBakedModel = null;
    private boolean loaded = false;

    public SocketModel(IModel socketModel) {
        this.socketModel = socketModel;
    }

    @Nullable
    @Override
    public IBakedModel bake(ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format) {
        if (!loaded) {
            IBakedModel bakedSocketModel = socketModel.bake(bakery, spriteGetter, sprite, format);
            if (bakedSocketModel != null) {
                socketFrameBakedModel = new SocketBakedModel(bakedSocketModel, spriteGetter);
            }
            if (socketFrameBakedModel == null) {
                socketFrameBakedModel = ModelLoaderRegistry.getMissingModel().bake(bakery, spriteGetter, sprite, format);
            }
            loaded = true;
        }
        return socketFrameBakedModel;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors) {
        return Collections.emptyList();
    }
}
