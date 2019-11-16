package xyz.brassgoggledcoders.reengineeredtoolbox.api.container;

import net.minecraft.entity.player.PlayerEntity;

public interface IFaceContainer {
    default void setup(ISocketContainer container) {

    }

    default boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
