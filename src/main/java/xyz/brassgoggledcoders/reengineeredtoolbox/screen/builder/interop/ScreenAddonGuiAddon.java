package xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.interop;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.socket.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenAddon;

import java.util.List;

public class ScreenAddonGuiAddon implements IGuiAddon {
    private final IScreenAddon screenAddon;

    public ScreenAddonGuiAddon(IScreenAddon screenAddon) {
        this.screenAddon = screenAddon;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY,
                                                int mouseX, int mouseY, float partialTicks) {
        if (screen instanceof ISocketScreen) {
            screenAddon.drawBackgroundLayer((ISocketScreen) screen, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY,
                                                int mouseX, int mouseY) {
        if (screen instanceof ISocketScreen) {
            screenAddon.drawForegroundLayer((ISocketScreen) screen, mouseX, mouseY);
        }
    }

    @Override
    public boolean isInside(Screen screen, double mouseX, double mouseY) {
        if (screen instanceof ISocketScreen) {
            return screenAddon.isInside((ISocketScreen) screen, mouseX, mouseY);
        }
        return false;
    }

    @Override
    public List<String> getTooltipLines() {
        return screenAddon.getTooltipLines();
    }
}
