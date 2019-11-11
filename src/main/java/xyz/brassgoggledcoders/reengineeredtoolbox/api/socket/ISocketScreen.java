package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;

public interface ISocketScreen {
    Screen getScreen();

    Container getContainer();
}
