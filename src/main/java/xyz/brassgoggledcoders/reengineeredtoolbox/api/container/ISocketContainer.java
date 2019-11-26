package xyz.brassgoggledcoders.reengineeredtoolbox.api.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public interface ISocketContainer {
    Slot addSlot(Slot slot);

    Container getContainer();

    PlayerInventory getPlayerInventory();
}
