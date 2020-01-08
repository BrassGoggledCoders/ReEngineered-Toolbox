package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.socket.ISocketScreen;

import javax.annotation.Nonnull;

public interface IFaceScreen {
    default void setup(@Nonnull ISocketScreen socketScreen) {

    }

    default void renderBackground(int mouseX, int mouseY, float partialTicks) {

    }

    default void renderForeground(int mouseX, int mouseY) {

    }
}
