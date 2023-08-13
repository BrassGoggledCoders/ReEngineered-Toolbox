package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.BlockPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredItemTags;

import java.util.List;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void addFrameSlotText(RenderGuiOverlayEvent.Post event) {
        if (Minecraft.getInstance().hitResult instanceof BlockHitResult blockHitResult) {
            Player player = Minecraft.getInstance().player;
            if (player != null && player.getMainHandItem().is(ReEngineeredItemTags.CAN_ALTER_FRAME_SLOT)) {
                if (player.getLevel().getBlockEntity(blockHitResult.getBlockPos()) instanceof FrameBlockEntity frameBlockEntity) {
                    IPanelPosition panelPosition = BlockPanelPosition.fromDirection(blockHitResult.getDirection());
                    PanelEntity panelEntity = frameBlockEntity.getPanelEntity(panelPosition);
                    if (panelEntity != null) {
                        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

                        List<FrameSlot> viewList = panelEntity.getFrameSlots();
                        for (FrameSlot frameSlotView : viewList) {
                            if (frameSlotView.getView().isInside(blockHitResult.getLocation(), blockHitResult.getDirection())) {
                                drawBlockOverlayText(event.getPoseStack(), new Component[]{frameSlotView.getName()}, scaledWidth, scaledHeight);
                            }
                        }
                    }
                }
            }
        }
    }

    //From: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/src/main/java/blusunrize/immersiveengineering/client/BlockOverlayUtils.java#L51
    public static void drawBlockOverlayText(PoseStack transform, Component[] text, int scaledWidth, int scaledHeight) {
        if (text != null && text.length > 0) {
            int i = 0;
            MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            Font font = Minecraft.getInstance().font;
            for (Component s : text) {
                if (s != null) {
                    font.drawInBatch(
                            Language.getInstance().getVisualOrder(s),
                            scaledWidth / 2F + 8, scaledHeight / 2F + 8 + (i++) * font.lineHeight, 0xffffffff, true,
                            transform.last().pose(), buffer, false, 0, 0xf000f0
                    );
                }
            }

            buffer.endBatch();
        }
    }
}
