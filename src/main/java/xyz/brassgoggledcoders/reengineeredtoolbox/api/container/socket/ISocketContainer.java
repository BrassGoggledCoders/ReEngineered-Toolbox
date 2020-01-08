package xyz.brassgoggledcoders.reengineeredtoolbox.api.container.socket;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public interface ISocketContainer {
    @Nonnull
    Slot addSlot(@Nonnull IItemHandler handler, int index, int xPos, int yPos);

    @Nonnull
    Slot addSlot(@Nonnull Slot slot);

    @Nonnull
    Container getContainer();

    @Nonnull
    PlayerInventory getPlayerInventory();

    @Nonnull
    IntReferenceHolder trackInt(@Nonnull IntReferenceHolder holder);

    void trackIntArray(@Nonnull IIntArray holders);
}
