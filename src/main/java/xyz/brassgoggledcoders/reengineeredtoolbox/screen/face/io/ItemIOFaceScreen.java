package xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.io;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemIOFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.interop.GuiAddonScreenAddon;

import javax.annotation.Nonnull;

public class ItemIOFaceScreen implements IFaceScreen {
    private final ItemIOFaceInstance faceInstance;

    public ItemIOFaceScreen(ItemIOFaceInstance faceInstance) {
        this.faceInstance = faceInstance;
    }

    @Override
    public void setup(@Nonnull ISocketScreen socketScreen) {
        faceInstance.getInventory().getGuiAddons()
                .forEach(guiAddon -> socketScreen.getScreenBuilder().withScreenAddon(new GuiAddonScreenAddon(guiAddon.create())));
    }
}
