package xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face;

import net.minecraft.entity.player.PlayerEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.socket.ISocketContainer;

public interface IFaceContainer {
    default void setup(ISocketContainer container) {

    }

    default boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
