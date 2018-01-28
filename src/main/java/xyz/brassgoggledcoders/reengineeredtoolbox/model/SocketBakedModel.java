package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import com.teamacronymcoders.base.client.models.ModelUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;
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
            IExtendedBlockState extendedState = (IExtendedBlockState) state;

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
            Face sideFace = faces[side.ordinal()];
            bakedQuads.add(ModelUtils.createBakedQuad(DefaultVertexFormats.ITEM, getVerticesForSide(sideFace, side),
                    side, sideFace.getSprite(), new double[]{2, 2, 14, 14}, new float[]{1, 1, 1, 1},
                    side.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE));

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

    private Vector3f[] getVerticesForSide(Face face, EnumFacing facing) {
        float xOffset = 0;
        float yOffset = 0;
        float zOffset = 0;

        Vector3f[] vertices = getDefaultVertices(facing);

        return vertices;
    }

    private Vector3f[] getDefaultVertices(EnumFacing facing) {
        Vector3f[] vertices = new Vector3f[4];
        switch (facing) {
            case DOWN:
                vertices[0] = new Vector3f(0.125F, 0, 0.125F);
                vertices[1] = new Vector3f(0.125F, 0, 0.875F);
                vertices[2] = new Vector3f(0.875F, 0, 0.875F);
                vertices[3] = new Vector3f(0.875F, 0, 0.125F);
                break;
            case UP:
                vertices[0] = new Vector3f(0.125F, 1, 0.125F);
                vertices[1] = new Vector3f(0.125F, 1, 0.875F);
                vertices[2] = new Vector3f(0.875F, 1, 0.875F);
                vertices[3] = new Vector3f(0.875F, 1, 0.125F);
                break;
            case NORTH:
                vertices[0] = new Vector3f(0.125F, 0.875F, 0);
                vertices[1] = new Vector3f(0.125F, 0.125F, 0);
                vertices[2] = new Vector3f(0.875F, 0.125F, 0);
                vertices[3] = new Vector3f(0.875F, 0.875F, 0);
                break;
            case EAST:
                vertices[0] = new Vector3f(1, 0.875F, 0.875F);
                vertices[1] = new Vector3f(1, 0.125F, 0.875F);
                vertices[2] = new Vector3f(1, 0.125F, 0.125F);
                vertices[3] = new Vector3f(1, 0.875F, 0.125F);
                break;
            case SOUTH:
                vertices[0] = new Vector3f(0.125F, 0.875F, 1);
                vertices[1] = new Vector3f(0.125F, 0.125F, 1);
                vertices[2] = new Vector3f(0.875F, 0.125F, 1);
                vertices[3] = new Vector3f(0.875F, 0.875F, 1);
                break;
            case WEST:
                vertices[0] = new Vector3f(0, 0.875F, 0.875F);
                vertices[1] = new Vector3f(0, 0.125F, 0.875F);
                vertices[2] = new Vector3f(0, 0.125F, 0.125F);
                vertices[3] = new Vector3f(0, 0.875F, 0.125F);
                break;
        }
        return vertices;
    }
}

