package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement.Type;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FrameBakedModel implements IBakedModel {
    private final IBakedModel frameModel;
    private final TextureAtlasSprite emptySprite;
    private final Function<RenderMaterial, TextureAtlasSprite> spriteFunction;

    public FrameBakedModel(IBakedModel frameModel, Function<RenderMaterial, TextureAtlasSprite> spriteFunction) {
        this.frameModel = frameModel;
        this.spriteFunction = spriteFunction;
        this.emptySprite = spriteFunction.apply(new RenderMaterial(
                PlayerContainer.BLOCK_ATLAS,
                ReEngineeredToolbox.rl("face/empty")
        ));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, @Nonnull Random random) {
        List<BakedQuad> bakedQuads = frameModel.getQuads(blockState, side, random);
        if (side != null) {
            handleSide(bakedQuads, side, EmptyModelData.INSTANCE);
        } else {
            for (Direction direction : Direction.values()) {
                handleSide(bakedQuads, direction, EmptyModelData.INSTANCE);
            }
        }
        return bakedQuads;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, @Nonnull Random random,
                                    @Nonnull IModelData extraData) {
        List<BakedQuad> bakedQuads = frameModel.getQuads(blockState, side, random, extraData);
        if (side != null) {
            handleSide(bakedQuads, side, extraData);
        } else {
            for (Direction direction : Direction.values()) {
                handleSide(bakedQuads, direction, extraData);
            }
        }
        return bakedQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return frameModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return frameModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return frameModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return frameModel.isCustomRenderer();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite getParticleIcon() {
        return frameModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return frameModel.getParticleTexture(data);
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return frameModel.getOverrides();
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        return frameModel.handlePerspective(cameraTransformType, mat);
    }

    private void handleSide(List<BakedQuad> bakedQuads, @Nonnull Direction side, IModelData modelData) {
        TextureAtlasSprite sprite = emptySprite;
        bakedQuads.add(createBakedQuad(DefaultVertexFormats.BLOCK, getDefaultVertices(side),
                side, sprite, new double[]{2, 2, 14, 14}, new float[]{1, 1, 1, 1},
                side.getAxisDirection() == Direction.AxisDirection.NEGATIVE, new float[]{1, 1, 1, 1}));
    }

    private Vector3d[] getDefaultVertices(Direction facing) {
        Vector3d[] vertices = new Vector3d[4];
        switch (facing) {
            case DOWN:
                vertices[0] = new Vector3d(0.125F, 0, 0.125F);
                vertices[1] = new Vector3d(0.125F, 0, 0.875F);
                vertices[2] = new Vector3d(0.875F, 0, 0.875F);
                vertices[3] = new Vector3d(0.875F, 0, 0.125F);
                break;
            case UP:
                vertices[0] = new Vector3d(0.125F, 1, 0.125F);
                vertices[1] = new Vector3d(0.125F, 1, 0.875F);
                vertices[2] = new Vector3d(0.875F, 1, 0.875F);
                vertices[3] = new Vector3d(0.875F, 1, 0.125F);
                break;
            case NORTH:
                vertices[0] = new Vector3d(0.125F, 0.875F, 0);
                vertices[1] = new Vector3d(0.125F, 0.125F, 0);
                vertices[2] = new Vector3d(0.875F, 0.125F, 0);
                vertices[3] = new Vector3d(0.875F, 0.875F, 0);
                break;
            case EAST:
                vertices[0] = new Vector3d(1, 0.875F, 0.875F);
                vertices[1] = new Vector3d(1, 0.125F, 0.875F);
                vertices[2] = new Vector3d(1, 0.125F, 0.125F);
                vertices[3] = new Vector3d(1, 0.875F, 0.125F);
                break;
            case SOUTH:
                vertices[0] = new Vector3d(0.125F, 0.875F, 1);
                vertices[1] = new Vector3d(0.125F, 0.125F, 1);
                vertices[2] = new Vector3d(0.875F, 0.125F, 1);
                vertices[3] = new Vector3d(0.875F, 0.875F, 1);
                break;
            case WEST:
                vertices[0] = new Vector3d(0, 0.875F, 0.875F);
                vertices[1] = new Vector3d(0, 0.125F, 0.875F);
                vertices[2] = new Vector3d(0, 0.125F, 0.125F);
                vertices[3] = new Vector3d(0, 0.875F, 0.125F);
                break;
        }
        return vertices;
    }

    @SuppressWarnings("ConstantConditions")
    public static BakedQuad createBakedQuad(VertexFormat format, Vector3d[] vertices, Direction facing, TextureAtlasSprite sprite,
                                            double[] uvs, float[] colour, boolean invert, float[] alpha) {
        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        Vector3i normalInt = facing.getNormal();
        Vector3d faceNormal = new Vector3d(normalInt.getX(), normalInt.getY(), normalInt.getZ());
        int vId = invert ? 3 : 0;
        int u = vId > 1 ? 2 : 0;
        putVertexData(format, builder, vertices[vId], faceNormal, uvs[u], uvs[1], sprite, colour, alpha[vId]);
        vId = invert ? 2 : 1;
        u = vId > 1 ? 2 : 0;
        putVertexData(format, builder, vertices[vId], faceNormal, uvs[u], uvs[3], sprite, colour, alpha[vId]);
        vId = invert ? 1 : 2;
        u = vId > 1 ? 2 : 0;
        putVertexData(format, builder, vertices[vId], faceNormal, uvs[u], uvs[3], sprite, colour, alpha[vId]);
        vId = invert ? 0 : 3;
        putVertexData(format, builder, vertices[vId], faceNormal, uvs[u], uvs[1], sprite, colour, alpha[vId]);
        return builder.build();
    }

    public static void putVertexData(VertexFormat format, BakedQuadBuilder builder, Vector3d pos, Vector3d faceNormal,
                                     double u, double v, TextureAtlasSprite sprite, float[] colour, float alpha) {
        for (int e = 0; e < format.getElements().size(); e++)
            switch (format.getElements().get(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) pos.x, (float) pos.y, (float) pos.z);
                    break;
                case COLOR:
                    float d = 1;//LightUtil.diffuseLight(faceNormal.x, faceNormal.y, faceNormal.z);
                    builder.put(e, d * colour[0], d * colour[1], d * colour[2], 1 * colour[3] * alpha);
                    break;
                case UV:
                    if (format.getElements().get(e).getType() == Type.FLOAT) {
                        builder.put(e, sprite.getU(u), sprite.getV(v));
                    } else {
                        builder.put(e, 0, 0);
                    }
                    break;
                case NORMAL:
                    builder.put(e, (float) faceNormal.x(), (float) faceNormal.y(), (float) faceNormal.z());
                    break;
                default:
                    builder.put(e);
            }
    }

}
