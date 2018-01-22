package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class SocketModel implements IModel {
    private final IModel socketBaseModel;
    private IBakedModel socketBaseBakedModel = null;

    public SocketModel(IModel socketBaseModel) {
        this.socketBaseModel = socketBaseModel;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        if (socketBaseBakedModel == null) {
            socketBaseBakedModel = socketBaseModel.bake(state, format, bakedTextureGetter);
        }
        return new SocketBakedModel(socketBaseBakedModel);
    }
}
