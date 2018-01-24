package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SocketBakedModel implements IBakedModel {
    private final IBakedModel socketBaseBakedModel;

    public SocketBakedModel(IBakedModel socketBaseBakedModel) {
        this.socketBaseBakedModel = socketBaseBakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bakedQuads = socketBaseBakedModel.getQuads(state, side, rand);
        if (side != null) {
            
        }
        return bakedQuads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return socketBaseBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return socketBaseBakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return socketBaseBakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return socketBaseBakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return socketBaseBakedModel.getOverrides();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return socketBaseBakedModel.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return socketBaseBakedModel.handlePerspective(cameraTransformType);
    }
}
