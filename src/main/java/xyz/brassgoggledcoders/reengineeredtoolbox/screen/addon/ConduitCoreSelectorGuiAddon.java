package xyz.brassgoggledcoders.reengineeredtoolbox.screen.addon;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler.FaceMode;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.addon.BasicGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;

import java.awt.*;
import java.util.List;

public class ConduitCoreSelectorGuiAddon extends BasicGuiAddon implements IClickable {
    private final ConduitClient<?, ?, ?> conduitClient;
    private final List<IGuiAddon> buttons;

    private int xSize;
    private int ySize;
    private boolean clicked;
    private Point inventoryPosition;

    public ConduitCoreSelectorGuiAddon(ConduitClient<?, ?, ?> conduitClient, int posX, int posY) {
        super(posX, posY);
        this.conduitClient = conduitClient;
        this.buttons = Lists.newArrayList();
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider assetProvider, int guiX, int guiY,
                                                int mouseX, int mouseY, float partialTicks) {
        IBackgroundAsset backgroundInfo = assetProvider.getAsset(AssetTypes.BACKGROUND);
        IAsset buttonManager = assetProvider.getAsset(AssetTypes.BUTTON_SIDENESS_MANAGER);

        if (backgroundInfo != null && buttonManager != null) {
            this.xSize = buttonManager.getArea().width;
            this.ySize = buttonManager.getArea().height;

            GlStateManager.color4f(1, 1, 1, 1);

            AssetUtil.drawAsset(screen, buttonManager, guiX + getPosX(), guiY + getPosY());

            int offset = 2;
            AbstractGui.fill(guiX + getPosX() + offset, guiY + getPosY() + offset,
                    guiX + getPosX() + getXSize() - offset, guiY + getPosY() + getYSize() - offset,
                    -1);
            GlStateManager.color4f(1, 1, 1, 1);
            inventoryPosition = backgroundInfo.getInventoryPosition();
            if (isClicked()) {
                //draw the overlay for the slots
                screen.blit(guiX + inventoryPosition.x - 1, guiY + inventoryPosition.y - 1,
                        16, 213 + 18, 14, 14);
                screen.blit(guiX + inventoryPosition.x - 1, guiY + inventoryPosition.y - 1,
                        56, 185, 162, 54);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider assetProvider, int guiX, int guiY,
                                                int mouseX, int mouseY) {
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        if (screen instanceof IGuiAddonConsumer) {
            IGuiAddonConsumer guiAddonConsumer = (IGuiAddonConsumer) screen;
            for (IGuiAddon addon : guiAddonConsumer.getAddons()) {
                if (addon instanceof ConduitCoreSelectorGuiAddon && addon != this) {
                    ((ConduitCoreSelectorGuiAddon) addon).setClicked(guiAddonConsumer, false);
                }
            }
            this.setClicked(guiAddonConsumer, !clicked);
            if (clicked) {
                StateButtonAddon addon = new StateButtonAddon(new PosButton(inventoryPosition.x + 73,
                        inventoryPosition.y + 19, 14, 14), FaceMode.NONE.getInfo(),
                        FaceMode.ENABLED.getInfo()) {
                    @Override
                    public int getState() {
                        return 0;
                    }

                    @Override
                    public void handleClick(Screen gui, int guiX, int guiY, double mouseX, double mouseY, int mouse) {

                    }

                    @Override
                    public List<String> getTooltipLines() {
                        return Lists.newArrayList();
                    }
                };
                buttons.add(addon);
                guiAddonConsumer.getAddons().add(addon);
            }
        }
    }


    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    public boolean isClicked() {
        return clicked;
    }

    public <T extends IGuiAddonConsumer> void setClicked(T gui, boolean clicked) {
        this.clicked = clicked;
        if (!clicked) {
            gui.getAddons().removeIf(buttons::contains);
            buttons.clear();
            //TODO Handle Disable
            // gui.getContainer().setDisabled(false);
        }
    }
}
