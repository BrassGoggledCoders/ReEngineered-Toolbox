package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SocketBakedModel implements IBakedModel {
    private final IBakedModel socketFrameBakedModel;
    private final Function<ResourceLocation, TextureAtlasSprite> spriteFunction;

    public SocketBakedModel(IBakedModel socketFrameBakedModel, Function<ResourceLocation, TextureAtlasSprite> spriteFunction1) {
        this.socketFrameBakedModel = socketFrameBakedModel;
        this.spriteFunction = spriteFunction1;
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = socketFrameBakedModel.getQuads(state, side, rand, extraData);


        if (side != null) {
            handleSide(quads, side, extendedState, spriteFunction);
        } else {
            for (EnumFacing facing : EnumFacing.values()) {
                handleSide(bakedQuads, facing, extendedState, spriteFunction);
            }
        }

        return quads;
    }

    private void handleSide(List<BakedQuad> bakedQuads, Direction side, IModelData data, Function<ResourceLocation, TextureAtlasSprite> spriteFunction) {
        Face face = data.getData(ModelProperties.)
        if (faces != null) {
            Face sideFace = faces[side.ordinal()];
            bakedQuads.add(ModelUtils.createBakedQuad(DefaultVertexFormats.ITEM, getDefaultVertices(side),
                    side, sideFace.getSprite(), new double[]{2, 2, 14, 14}, new float[]{1, 1, 1, 1},
                    side.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE));

        }
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
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite getParticleTexture() {
        return socketFrameBakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return socketFrameBakedModel.getOverrides();
    }
}

