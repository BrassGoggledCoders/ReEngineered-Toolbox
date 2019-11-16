package xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.SlotsGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class SlotsScreenAddon implements IGuiAddon {
    private final int posX;
    private final int posY;
    private final int slotCount;
    private final int color;
    private final Function<Integer, Pair<Integer, Integer>> slotPosition;

    public SlotsScreenAddon(int posX, int posY, int slotCount, int color, Function<Integer, Pair<Integer, Integer>> slotPosition) {
        this.posX = posX;
        this.posY = posY;
        this.slotCount = slotCount;
        this.color = color;
        this.slotPosition = slotPosition;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY,
                                                int mouseX, int mouseY, float partialTicks) {
        SlotsGuiAddon.drawAsset(screen, iAssetProvider, guiX, guiY, posX, posY, slotCount, slotPosition,
                color > 0, color);
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider iAssetProvider, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean isInside(Screen screen, double v, double v1) {
        return false;
    }
}
