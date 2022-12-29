package xyz.brassgoggledcoders.reengineeredtoolbox.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.SingleTypedSlotMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.slot.TypedMenuSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotRenderProperties;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotRenderProperties;

public class SingleTypedSlotScreen<T extends SingleTypedSlotMenu<U, V>, U extends ITypedSlot<V>, V> extends AbstractContainerScreen<T> {
    private static final ResourceLocation BACKGROUND = ReEngineeredToolbox.rl("textures/screen/single_slot_with_player.png");

    public SingleTypedSlotScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY) {
        for (int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = this.menu.slots.get(k);
            if (slot instanceof TypedMenuSlot typedMenuSlot) {
                ITypedSlot<?> typedSlot = typedMenuSlot.getTypedSlot();
                ITypedSlotRenderProperties renderProperties = TypedSlotRenderProperties.getProperties(typedSlot);

                if (renderProperties != null) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    renderProperties.renderSlot(this, pPoseStack, slot.x, slot.y, typedSlot);

                    if (this.isHovering(typedMenuSlot.x, typedMenuSlot.y, 16, 16, pMouseX, pMouseY)) {
                        this.hoveredSlot = slot;
                        int l = slot.x;
                        int i1 = slot.y;
                        renderSlotHighlight(pPoseStack, l, i1, this.getBlitOffset(), this.getSlotColor(k));
                    }
                }
            }
        }
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    public static <T extends SingleTypedSlotMenu<U, V>, U extends ITypedSlot<V>, V> SingleTypedSlotScreen<T, U, V> create(
            T menu, Inventory inv, Component displayName) {
        return new SingleTypedSlotScreen<>(menu, inv, displayName);
    }
}
