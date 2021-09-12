package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SocketBakedModel implements IBakedModel {
    private final IBakedModel socketFrameBakedModel;
    private final TextureAtlasSprite emptySprite;
    private final Function<ResourceLocation, TextureAtlasSprite> spriteFunction;

    public SocketBakedModel(IBakedModel socketFrameBakedModel, Function<ResourceLocation, TextureAtlasSprite> spriteFunction) {
        this.socketFrameBakedModel = socketFrameBakedModel;
        this.spriteFunction = spriteFunction;
        this.emptySprite = spriteFunction.apply(new ResourceLocation(ReEngineeredToolbox.ID, "faces/empty"));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = socketFrameBakedModel.getQuads(state, side, rand, extraData);

        if (side != null) {
            handleSide(quads, side, extraData);
        } else {
            for (Direction direction : Direction.values()) {
                handleSide(quads, direction, extraData);
            }
        }

        return quads;
    }

    private void handleSide(List<BakedQuad> bakedQuads, @Nonnull Direction side, IModelData modelData) {
        FaceInstance faceInstance = modelData.getData(FaceProperty.getModelForSide(side));
        TextureAtlasSprite sprite = faceInstance == null ? emptySprite : spriteFunction.apply(faceInstance.getSpriteLocation());
        bakedQuads.add(createBakedQuad(getDefaultVertices(side),
                side, sprite, new double[]{2, 2, 14, 14}, new float[]{1, 1, 1, 1},
                side.getAxisDirection() == Direction.AxisDirection.NEGATIVE, new float[]{1, 1, 1, 1}));
    }

    private Vector3f[] getDefaultVertices(Direction facing) {
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

    private BakedQuad createBakedQuad(Vector3f[] vertices, Direction facing, TextureAtlasSprite sprite, double[] uvs,
                                      float[] colour, boolean invert, float[] alpha) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
        builder.setQuadOrientation(facing);
        builder.setTexture(sprite);
        Normal faceNormal = new Normal(facing.getDirectionVec().getX(), facing.getDirectionVec().getY(), facing.getDirectionVec().getZ());
        int vId = invert ? 3 : 0;
        int u = vId > 1 ? 2 : 0;
        putVertexData(builder, vertices[vId], faceNormal, uvs[u], uvs[1], sprite, colour, alpha[invert ? 3 : 0]);
        vId = invert ? 2 : 1;
        u = vId > 1 ? 2 : 0;
        putVertexData(builder, vertices[invert ? 2 : 1], faceNormal, uvs[u], uvs[3], sprite, colour, alpha[invert ? 2 : 1]);
        vId = invert ? 1 : 2;
        u = vId > 1 ? 2 : 0;
        putVertexData(builder, vertices[invert ? 1 : 2], faceNormal, uvs[u], uvs[3], sprite, colour, alpha[invert ? 1 : 2]);
        vId = invert ? 1 : 3;
        u = vId > 1 ? 2 : 0;
        putVertexData(builder, vertices[invert ? 0 : 3], faceNormal, uvs[u], uvs[1], sprite, colour, alpha[invert ? 0 : 3]);
        return builder.build();
    }

    private void putVertexData(UnpackedBakedQuad.Builder builder, Vector3f pos, Normal faceNormal, double u, double v,
                               TextureAtlasSprite sprite, float[] colour, float alpha) {
        for (int e = 0; e < DefaultVertexFormats.ITEM.getElementCount(); e++) {
            switch (DefaultVertexFormats.ITEM.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, pos.getX(), pos.getY(), pos.getZ(), 0);
                    break;
                case COLOR:
                    float d = 1;
                    builder.put(e, d * colour[0], d * colour[1], d * colour[2], 1 * colour[3] * alpha);
                    break;
                case UV:
                    builder.put(e, sprite.getInterpolatedU(u), sprite.getInterpolatedV((v)), 0, 1);
                    break;
                case NORMAL:
                    builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                    break;
                default:
                    builder.put(e);
            }
        }
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return socketFrameBakedModel.getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return socketFrameBakedModel.getOverrides();
    }
}

