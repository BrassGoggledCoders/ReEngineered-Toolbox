package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class FrameBakedModel implements IBakedModel {
    private final IBakedModel frameModel;

    public FrameBakedModel(IBakedModel frameModel) {
        this.frameModel = frameModel;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, @Nonnull Random pRand) {
        return frameModel.getQuads(pState, pSide, pRand);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        return frameModel.getQuads(state, side, rand, extraData);
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
}
