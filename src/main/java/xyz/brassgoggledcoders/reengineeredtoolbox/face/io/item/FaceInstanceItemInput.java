package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.teamacronymcoders.base.capability.item.ItemStackHandlerImport;
import com.teamacronymcoders.base.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstanceItemInput extends FaceInstance {
    private IItemHandlerModifiable currentStack = new ItemStackHandlerImport(1);
    private int itemQueueInput = 0;

    @Override
    public void onTick(ISocketTile tile) {
        ItemStack itemStack = currentStack.getStackInSlot(0);
        ItemStackQueue queue = tile.getItemStackQueue(itemQueueInput);
        if (!itemStack.isEmpty()) {
            itemStack = queue.offer(itemStack);
            currentStack.setStackInSlot(0, itemStack);
        }
    }

    @Override
    public void configureQueue(String name, int queueNumber) {
        if ("itemQueueInput".equals(name)) {
            itemQueueInput = queueNumber;
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return hasCapability(capability) ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(currentStack) : null;
    }
}
