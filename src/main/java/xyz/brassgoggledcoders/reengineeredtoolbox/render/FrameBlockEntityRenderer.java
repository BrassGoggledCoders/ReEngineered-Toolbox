package xyz.brassgoggledcoders.reengineeredtoolbox.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;

public class FrameBlockEntityRenderer implements BlockEntityRenderer<FrameBlockEntity> {
    private final static ResourceLocation BUTTONS = ReEngineeredToolbox.rl("textures/block/frame_buttons.png");

    public FrameBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredContext) {

    }

    @Override
    public void render(@NotNull FrameBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        HitResult hitResult = Minecraft.getInstance().hitResult;

        if (hitResult instanceof BlockHitResult blockHitResult && blockHitResult.getBlockPos().equals(pBlockEntity.getFramePos())) {
            pPoseStack.pushPose();

            VertexConsumer consumer = pBufferSource.getBuffer(RenderType.entitySolid(BUTTONS));
            Matrix4f pose = pPoseStack.last().pose();
            Matrix3f normal = pPoseStack.last().normal();
            float[] color = DyeColor.BLACK.getTextureDiffuseColors();



            pPoseStack.popPose();
        }
    }

    @Override
    public int getViewDistance() {
        return 8;
    }
}
