package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.block.RedStoneWireBlock;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotRenderProperties;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class RedstoneSlotType extends TypedSlotType {
    private static final ResourceLocation REDSTONE_DOT = new ResourceLocation("block/redstone_dust_dot");

    public RedstoneSlotType() {
        super(typedSlotHolder -> null, RedstoneTypedSlot::new);
    }

    @Override
    public void initializeClient(Consumer<ITypedSlotRenderProperties> consumer) {
        consumer.accept(new ITypedSlotRenderProperties() {

            @Override
            public void renderSlot(Screen screen, PoseStack poseStack, int slotX, int slotY, ITypedSlot<?> typedSlot) {
                TextureAtlasSprite sprite = Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(REDSTONE_DOT);
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

                int power = 0;
                if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                    power = redstoneTypedSlot.getContent().getAsInt();
                }
                Color color = new Color(RedStoneWireBlock.getColorForPower(power));

                RenderSystem.setShaderColor(
                        (float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F,
                        (float) color.getBlue() / 255.0F,
                        (float) color.getAlpha() / 255.0F
                );

                RenderSystem.enableBlend();
                GuiComponent.blit(poseStack, slotX, slotY, 0, sprite.getWidth(), sprite.getHeight(), sprite);
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            @Override
            public void getTooltip(List<TooltipComponent> tooltipComponentList, ITypedSlot<?> typedSlot) {

            }
        });
    }
}
