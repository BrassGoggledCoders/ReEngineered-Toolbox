package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate.PanelModelBakery;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrameBakedModel implements BakedModel {
    private final BakedModel frameModel;
    private final ItemOverrides itemOverrides;

    public FrameBakedModel(BakedModel frameModel) {
        this.frameModel = frameModel;
        this.itemOverrides = new FrameItemOverrides();
    }

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, @NotNull RandomSource pRandom, @NotNull ModelData modelData, RenderType renderType) {
        List<BakedQuad> bakedQuads = new ArrayList<>(frameModel.getQuads(pState, pDirection, pRandom, ModelData.EMPTY, renderType));
        for (Direction direction : Direction.values()) {
            ModelProperty<PanelState> modelProperty = FrameBlockEntity.PANEL_STATE_MODEL_PROPERTIES.get(direction);
            PanelState panelState = modelData.get(modelProperty);
            if (panelState == null) {
                panelState = ReEngineeredPanels.PLUG.get()
                        .defaultPanelState();
            }

            bakedQuads.addAll(PanelModelBakery.getInstance()
                    .getPanelStateModel(panelState, direction)
                    .getQuads(pState, pDirection, pRandom, ModelData.EMPTY, renderType)
            );
        }

        return bakedQuads;
    }

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, @NotNull RandomSource pRandom) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    @NotNull
    public TextureAtlasSprite getParticleIcon() {
        return frameModel.getParticleIcon(ModelData.EMPTY);
    }

    @Override
    @NotNull
    public ItemOverrides getOverrides() {
        return this.itemOverrides;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BakedModel applyTransform(ItemTransforms.TransformType transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        this.frameModel.applyTransform(transformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    @NotNull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.union(
                this.frameModel.getRenderTypes(state, rand, data),
                ChunkRenderTypeSet.of(RenderType.solid())
        );
    }
}
