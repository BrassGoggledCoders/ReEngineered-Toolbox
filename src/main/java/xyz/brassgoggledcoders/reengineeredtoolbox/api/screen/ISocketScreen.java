package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenBuilder;

public interface ISocketScreen {
    IScreenBuilder getScreenBuilder();

    Screen getScreen();

    Container getContainer();
}
