package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class SocketBakedModel implements IBakedModel {
    private final IBakedModel socketFrameBakedModel;
    private final Function<ResourceLocation, TextureAtlasSprite> spriteFunction;


    public SocketBakedModel(IBakedModel socketFrameBakedModel, Function<ResourceLocation, TextureAtlasSprite> spriteFunction1) {
        this.socketFrameBakedModel = socketFrameBakedModel;
        this.spriteFunction = spriteFunction1;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bakedQuads = socketFrameBakedModel.getQuads(state, side, rand);

        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState)state;

            if (side != null) {
                handleSide(bakedQuads, side, extendedState, spriteFunction);
            } else {
                for (EnumFacing facing : EnumFacing.values()) {
                    handleSide(bakedQuads, facing, extendedState, spriteFunction);
                }
            }
        }

        return bakedQuads;
    }

    private void handleSide(List<BakedQuad> bakedQuads, EnumFacing side, IExtendedBlockState state, Function<ResourceLocation, TextureAtlasSprite> spriteFunction) {
        Face[] faces = state.getValue(BlockSocket.SIDED_FACE_PROPERTY);
        if (faces != null) {
            bakedQuads.addAll(faces[side.ordinal()].getModel(side, spriteFunction));
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        return socketFrameBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return socketFrameBakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return socketFrameBakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return socketFrameBakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return socketFrameBakedModel.getOverrides();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return socketFrameBakedModel.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return socketFrameBakedModel.handlePerspective(cameraTransformType);
    }
}
