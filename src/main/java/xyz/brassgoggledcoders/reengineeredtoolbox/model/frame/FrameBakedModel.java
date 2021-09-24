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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.PanelInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETPanels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FrameBakedModel implements IBakedModel {
    private final IBakedModel frameModel;
    private final TextureAtlasSprite emptySprite;
    private final Function<RenderMaterial, TextureAtlasSprite> spriteFunction;
    private final BiFunction<PanelState, Direction, IBakedModel> panelFunction;

    public FrameBakedModel(IBakedModel frameModel, Function<RenderMaterial, TextureAtlasSprite> spriteFunction,
                           BiFunction<PanelState, Direction, IBakedModel> panelFunction) {
        this.frameModel = frameModel;
        this.spriteFunction = spriteFunction;
        this.emptySprite = spriteFunction.apply(new RenderMaterial(
                PlayerContainer.BLOCK_ATLAS,
                ReEngineeredToolbox.rl("panel/empty")
        ));
        this.panelFunction = panelFunction;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, @Nonnull Random random) {
        return frameModel.getQuads(blockState, side, random);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, @Nonnull Random random,
                                    @Nonnull IModelData extraData) {
        List<BakedQuad> bakedQuads = frameModel.getQuads(blockState, side, random, extraData);
        if (side != null) {
            PanelInfo panelInfo = extraData.getData(FrameModelProperty.getModelForSide(side));
            PanelState panelState = panelInfo != null ? panelInfo.getPanelState() : RETPanels.OPEN.get().getDefaultState();
            bakedQuads.addAll(panelFunction.apply(panelState, side)
                    .getQuads(blockState, null, random, extraData));
        } else {
            for (Direction direction : Direction.values()) {
                PanelInfo panelInfo = extraData.getData(FrameModelProperty.getModelForSide(direction));
                PanelState panelState = panelInfo != null ? panelInfo.getPanelState() : RETPanels.OPEN.get().getDefaultState();
                bakedQuads.addAll(panelFunction.apply(panelState, direction)
                        .getQuads(blockState, null, random, extraData));
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
}
