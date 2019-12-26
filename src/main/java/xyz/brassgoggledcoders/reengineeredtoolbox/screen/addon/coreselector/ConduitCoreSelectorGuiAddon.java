package xyz.brassgoggledcoders.reengineeredtoolbox.screen.addon.coreselector;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.addon.BasicGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.IConduitManager;

import java.awt.*;
import java.util.List;

public class ConduitCoreSelectorGuiAddon<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends BasicGuiAddon implements IClickable {
    private final IConduitManager conduitManager;
    private final ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient;
    private final List<IGuiAddon> buttons;

    private int xSize;
    private int ySize;
    private boolean clicked;
    private Point inventoryPosition;

    public ConduitCoreSelectorGuiAddon(IConduitManager conduitManager, ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient,
                                       int posX, int posY) {
        super(posX, posY);
        this.conduitManager = conduitManager;
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
                    ((ConduitCoreSelectorGuiAddon<?, ?, ?>) addon).setClicked(guiAddonConsumer, false);
                }
            }
            this.setClicked(guiAddonConsumer, !clicked);
            if (clicked) {
                int xPos = inventoryPosition.x + 55;
                for (ConduitCore<CONTENT, CONTEXT, TYPE> conduitCore : conduitManager.getCoresFor(conduitClient.getConduitType())) {
                    StateButtonAddon addon = new ConduitCoreSelectorButtonStateGuiAddon<>(conduitClient, conduitCore,
                            xPos += 18, inventoryPosition.y + 19);
                    buttons.add(addon);
                    guiAddonConsumer.getAddons().add(addon);
                }

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
