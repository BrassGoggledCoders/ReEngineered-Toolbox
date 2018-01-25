package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SocketBakedModel implements IBakedModel {
    private final IBakedModel socketframeBakedModel;

    public SocketBakedModel(IBakedModel socketframeBakedModel) {
        this.socketframeBakedModel = socketframeBakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bakedQuads = socketframeBakedModel.getQuads(state, side, rand);

        if (side != null) {
            handleSide(bakedQuads, side, state);
        } else {
            for (EnumFacing facing : EnumFacing.values()) {
                handleSide(bakedQuads, facing, state);
            }
        }
        return bakedQuads;
    }

    private void handleSide(List<BakedQuad> bakedQuads, EnumFacing side, @Nullable IBlockState state) {
        
    }

    @Override
    public boolean isAmbientOcclusion() {
        return socketframeBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return socketframeBakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return socketframeBakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return socketframeBakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return socketframeBakedModel.getOverrides();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return socketframeBakedModel.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return socketframeBakedModel.handlePerspective(cameraTransformType);
    }
}
