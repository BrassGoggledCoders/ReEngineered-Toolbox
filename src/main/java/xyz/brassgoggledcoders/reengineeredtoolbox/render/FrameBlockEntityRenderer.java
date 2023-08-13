package xyz.brassgoggledcoders.reengineeredtoolbox.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.textures.UnitTextureAtlasSprite;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotView;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.BlockPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredItemTags;

import java.util.List;

public class FrameBlockEntityRenderer implements BlockEntityRenderer<FrameBlockEntity> {
    private final static ResourceLocation BUTTONS = ReEngineeredToolbox.rl("textures/block/frame_buttons.png");

    public FrameBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredContext) {

    }

    @Override
    public void render(@NotNull FrameBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().is(ReEngineeredItemTags.CAN_ALTER_FRAME_SLOT)) {
            if (hitResult instanceof BlockHitResult blockHitResult && blockHitResult.getBlockPos().equals(pBlockEntity.getFramePos())) {
                Direction direction = blockHitResult.getDirection();
                IPanelPosition panelPosition = BlockPanelPosition.fromDirection(direction);
                PanelEntity panelEntity = pBlockEntity.getPanelEntity(panelPosition);
                if (panelEntity != null) {
                    List<FrameSlot> frameSlotView = panelEntity.getFrameSlots();
                    if (!frameSlotView.isEmpty()) {

                        BlockPos offset = panelEntity.getBlockPos().relative(direction, 1);
                        int packed = LightTexture.pack(
                                panelEntity.getLevel().getBrightness(LightLayer.BLOCK, offset),
                                panelEntity.getLevel().getBrightness(LightLayer.SKY, offset)
                        );

                        for (FrameSlot frameSlot : frameSlotView) {
                            Pair<Vector3f, Vector3f> toFrom = cubeLocation(frameSlot.getView(), direction);
                            if (toFrom != null) {
                                pPoseStack.pushPose();
                                putTexturedQuad(
                                        pBufferSource.getBuffer(RenderType.entityTranslucent(BUTTONS)),
                                        pPoseStack.last().pose(),
                                        pPoseStack.last().normal(),
                                        UnitTextureAtlasSprite.INSTANCE,
                                        toFrom.getFirst(),
                                        toFrom.getSecond(),
                                        direction,
                                        adjustAlpha(frameSlot.getFrequency().getColor().getTextColor(), 192),
                                        packed,
                                        0,
                                        false
                                );
                                pPoseStack.popPose();
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getViewDistance() {
        return 6;
    }

    private Pair<Vector3f, Vector3f> cubeLocation(FrameSlotView slotView, Direction direction) {
        return switch (direction) {
            case UP -> Pair.of(
                    new Vector3f(slotView.xPos() / 16F, 1.0001F, slotView.yPos() / 16F),
                    new Vector3f((slotView.xPos() + slotView.width()) / 16F, 1.0001F, (slotView.yPos() + slotView.height()) / 16F)
            );
            case DOWN -> Pair.of(
                    new Vector3f((16 - slotView.xPos() - slotView.width()) / 16F, -0.0001F, slotView.yPos() / 16F),
                    new Vector3f((16 - slotView.xPos()) / 16F, -0.0001F, (slotView.yPos() + slotView.height()) / 16F)
            );
            case NORTH -> Pair.of(
                    new Vector3f((16 - slotView.xPos() - slotView.width()) / 16F, (16 - slotView.yPos() - slotView.height()) / 16F, -0.0001F),
                    new Vector3f((16 - slotView.xPos()) / 16F, (16 - slotView.yPos()) / 16F, -0.0001F)
            );
            case SOUTH -> Pair.of(
                    new Vector3f(slotView.xPos() / 16F, (16 - slotView.yPos() - slotView.height()) / 16F, 1.0001F),
                    new Vector3f((slotView.xPos() + slotView.width()) / 16F, (16 - slotView.yPos()) / 16F, 1.0001F)
            );
            case WEST -> Pair.of(
                    new Vector3f(-0.0001F, (16 - slotView.yPos() - slotView.height()) / 16F, slotView.xPos() / 16F),
                    new Vector3f(-0.0001F, (16 - slotView.yPos()) / 16F, (slotView.xPos() + slotView.width()) / 16F)
            );
            case EAST -> Pair.of(
                    new Vector3f(1.0001F, (16 - slotView.yPos() - slotView.height()) / 16F, (16 - slotView.xPos() - slotView.width()) / 16F),
                    new Vector3f(1.0001F, (16 - slotView.yPos()) / 16F, (16 - slotView.xPos()) / 16F)
            );
        };
    }

    public static int adjustAlpha(int color, int alpha) {
        return (color & 0xffffff) | (alpha << 24);
    }

    /* From: https://github.com/SlimeKnights/Mantle/blob/1.18.2/src/main/java/slimeknights/mantle/client/render/FluidRenderer.java*/

    /**
     * Adds a quad to the renderer
     *
     * @param renderer   Renderer instance
     * @param matrix     Render matrix
     * @param sprite     Sprite to render
     * @param from       Quad start
     * @param to         Quad end
     * @param face       Face to render
     * @param color      Color to use in rendering
     * @param brightness Face brightness
     * @param flowing    If true, half texture coordinates
     */
    public static void putTexturedQuad(VertexConsumer renderer, Matrix4f matrix, Matrix3f pNormal, TextureAtlasSprite sprite, Vector3f from,
                                       Vector3f to, Direction face, int color, int brightness, int rotation, boolean flowing) {
        // start with texture coordinates
        float x1 = from.x(), y1 = from.y(), z1 = from.z();
        float x2 = to.x(), y2 = to.y(), z2 = to.z();
        // choose UV based on the directions, some need to negate UV due to the direction
        // note that we use -UV instead of 1-UV as its slightly simpler and the later logic deals with negatives
        float u1, u2, v1, v2;
        switch (face) {
            default -> { // DOWN
                u1 = x1;
                u2 = x2;
                v1 = z2;
                v2 = z1;
            }
            case UP -> {
                u1 = x1;
                u2 = x2;
                v1 = -z1;
                v2 = -z2;
            }
            case NORTH -> {
                u1 = -x1;
                u2 = -x2;
                v1 = y1;
                v2 = y2;
            }
            case SOUTH -> {
                u1 = x2;
                u2 = x1;
                v1 = y1;
                v2 = y2;
            }
            case WEST -> {
                u1 = z2;
                u2 = z1;
                v1 = y1;
                v2 = y2;
            }
            case EAST -> {
                u1 = -z1;
                u2 = -z2;
                v1 = y1;
                v2 = y2;
            }
        }

        // flip V when relevant
        if (rotation == 0 || rotation == 270) {
            float temp = v1;
            v1 = -v2;
            v2 = -temp;
        }
        // flip U when relevant
        if (rotation >= 180) {
            float temp = u1;
            u1 = -u2;
            u2 = -temp;
        }

        // bound UV to be between 0 and 1
        boolean reverse = u1 > u2;
        u1 = boundUV(u1, reverse);
        u2 = boundUV(u2, !reverse);
        reverse = v1 > v2;
        v1 = boundUV(v1, reverse);
        v2 = boundUV(v2, !reverse);

        // if rotating by 90 or 270, swap U and V
        float minU, maxU, minV, maxV;
        double size = flowing ? 8 : 16;
        if ((rotation % 180) == 90) {
            minU = sprite.getU(v1 * size);
            maxU = sprite.getU(v2 * size);
            minV = sprite.getV(u1 * size);
            maxV = sprite.getV(u2 * size);
        } else {
            minU = sprite.getU(u1 * size);
            maxU = sprite.getU(u2 * size);
            minV = sprite.getV(v1 * size);
            maxV = sprite.getV(v2 * size);
        }
        // based on rotation, put coords into place
        float u3, u4, v3, v4;
        switch (rotation) {
            default -> { // 0
                u1 = minU;
                v1 = maxV;
                u2 = minU;
                v2 = minV;
                u3 = maxU;
                v3 = minV;
                u4 = maxU;
                v4 = maxV;
            }
            case 90 -> {
                u1 = minU;
                v1 = minV;
                u2 = maxU;
                v2 = minV;
                u3 = maxU;
                v3 = maxV;
                u4 = minU;
                v4 = maxV;
            }
            case 180 -> {
                u1 = maxU;
                v1 = minV;
                u2 = maxU;
                v2 = maxV;
                u3 = minU;
                v3 = maxV;
                u4 = minU;
                v4 = minV;
            }
            case 270 -> {
                u1 = maxU;
                v1 = maxV;
                u2 = minU;
                v2 = maxV;
                u3 = minU;
                v3 = minV;
                u4 = maxU;
                v4 = minV;
            }
        }
        // add quads
        int light1 = brightness & 0xFFFF;
        int light2 = brightness >> 0x10 & 0xFFFF;
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        switch (face) {
            case DOWN -> {
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            case UP -> {
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            case NORTH -> {
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            case SOUTH -> {
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            case WEST -> {
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            case EAST -> {
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light1, light2).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
            }
        }
    }

    /**
     * Forces the UV to be between 0 and 1
     *
     * @param value Original value
     * @param upper If true, this is the larger UV. Needed to enforce integer values end up at 1
     * @return UV mapped between 0 and 1
     */
    private static float boundUV(float value, boolean upper) {
        value = value % 1;
        if (value == 0) {
            // if it lands exactly on the 0 bound, map that to 1 instead for the larger UV
            return upper ? 1 : 0;
        }
        // modulo returns a negative result if the input is negative, so add 1 to account for that
        return value < 0 ? (value + 1) : value;
    }


}
