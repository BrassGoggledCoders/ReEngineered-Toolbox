package xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.interop;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.socket.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenAddon;

import java.util.List;

public class GuiAddonScreenAddon implements IScreenAddon {
    private final IGuiAddon guiAddon;

    public GuiAddonScreenAddon(IGuiAddon guiAddon) {
        this.guiAddon = guiAddon;
    }

    public void drawBackgroundLayer(ISocketScreen socketScreen, int mouseX, int mouseY, float partialTicks) {
        guiAddon.drawGuiContainerBackgroundLayer(socketScreen.getScreen(), IAssetProvider.DEFAULT_PROVIDER,
                socketScreen.getScreen().height, socketScreen.getScreen().width, mouseX, mouseY, partialTicks);
    }

    public void drawForegroundLayer(ISocketScreen socketScreen, int mouseX, int mouseY) {
        guiAddon.drawGuiContainerForegroundLayer(socketScreen.getScreen(), IAssetProvider.DEFAULT_PROVIDER,
                socketScreen.getScreen().height, socketScreen.getScreen().width, mouseX, mouseY);
    }

    public List<String> getTooltipLines() {
        return guiAddon.getTooltipLines();
    }

    public boolean isInside(ISocketScreen screen, double mouseX, double mouseY) {
        return guiAddon.isInside(screen.getScreen(), mouseX, mouseY);
    }
}
