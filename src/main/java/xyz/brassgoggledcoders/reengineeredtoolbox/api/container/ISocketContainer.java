package xyz.brassgoggledcoders.reengineeredtoolbox.api.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.IItemHandler;

public interface ISocketContainer {
    Slot addSlot(IItemHandler handler, int index, int xPos, int yPos);

    Container getContainer();

    PlayerInventory getPlayerInventory();
}
