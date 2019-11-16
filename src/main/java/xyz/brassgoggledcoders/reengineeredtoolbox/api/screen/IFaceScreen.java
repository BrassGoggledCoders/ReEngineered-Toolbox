package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen;

import javax.annotation.Nonnull;

public interface IFaceScreen {
    default void setup(@Nonnull ISocketScreen socketScreen) {

    }

    default void renderBackground(int mouseX, int mouseY, float partialTicks) {

    }

    default void renderForeground(int mouseX, int mouseY, float partialTicks) {

    }
}
