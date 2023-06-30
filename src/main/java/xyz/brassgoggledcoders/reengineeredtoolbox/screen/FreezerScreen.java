package xyz.brassgoggledcoders.reengineeredtoolbox.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FreezerMenu;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.TankView;

import java.awt.*;

public class FreezerScreen extends AbstractContainerScreen<FreezerMenu> {
    private static final ResourceLocation TEXTURE = ReEngineeredToolbox.rl("textures/screen/freezer.png");
    public FreezerScreen(FreezerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderFluid(pPoseStack);
        this.renderProgressBar(pPoseStack);
        this.renderEnergyBar(pPoseStack);
    }

    private void renderFluid(@NotNull PoseStack poseStack) {
        TankView tankView = this.getMenu().getTankView();
        int x = leftPos + 44;
        int y = topPos + 17;
        int height = 52;
        int capacity = tankView.capacity();
        FluidStack fluidStack = tankView.fluidStack();
        if (!fluidStack.isEmpty()) {
            int stored = fluidStack.getAmount();
            if (stored > capacity) {
                stored = capacity;
            }
            int offset = stored * height / capacity;

            FluidType fluidType = fluidStack.getFluid()
                    .getFluidType();
            IClientFluidTypeExtensions clientFluidTypeExtensions = IClientFluidTypeExtensions.of(fluidType);
            ResourceLocation flowing = clientFluidTypeExtensions.getStillTexture(fluidStack);
            if (flowing != null) {
                TextureAtlasSprite flowingSprite = Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(flowing);
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

                Color color = new Color(clientFluidTypeExtensions.getTintColor(fluidStack));

                RenderSystem.setShaderColor(
                        (float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F,
                        (float) color.getBlue() / 255.0F,
                        (float) color.getAlpha() / 255.0F
                );
                RenderSystem.enableBlend();
                int startY = y + (fluidType.isAir() ? 0 : height - offset);
                blit(poseStack, x, startY, 0, 16, offset, flowingSprite);
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, x, y, 188, 0, 16, 52);
    }

    private void renderProgressBar(@NotNull PoseStack poseStack) {
        ProgressView progress = this.menu.getProgress();
        if (progress.current() > 0) {
            int offset = progress.getOffset(22);
            this.blit(poseStack, this.leftPos + 95, this.topPos + 35, 176, 57, offset + 1, 16);
        }
    }

    private void renderEnergyBar(@NotNull PoseStack poseStack) {
        ProgressView energy = this.menu.getEnergy();
        if (energy.current() > 0) {
            int offset = energy.getOffset(50);
            this.blit(poseStack, this.leftPos + 10, this.topPos + 18 + (50 - offset), 176, 50 - offset, 12, offset + 1);
        }
    }
}
