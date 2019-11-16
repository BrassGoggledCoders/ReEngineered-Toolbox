package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;

import java.util.Collections;
import java.util.List;

public interface IScreenAddon {
    default void drawBackgroundLayer(ISocketScreen socketScreen, int mouseX, int mouseY, float partialTicks) {

    }

    default void drawForegroundLayer(ISocketScreen socketScreen, int mouseX, int mouseY) {

    }

    default List<String> getTooltipLines() {
        return Collections.emptyList();
    }

    default boolean isInside(ISocketScreen screen, double mouseX, double mouseY) {
        return false;
    }
}
