package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.teamacronymcoders.base.capability.item.ItemStackHandlerImport;
import com.teamacronymcoders.base.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstanceItemInput extends FaceInstanceItemIO {

    public FaceInstanceItemInput() {
        super(new ItemStackHandlerImport());
    }

    @Override
    public void onTick(ISocketTile tile) {
        ItemStack itemStack = currentStack.getStackInSlot(0);
        ItemStackQueue queue = tile.getItemStackQueue(itemQueueNumber);
        if (!itemStack.isEmpty()) {
            itemStack = queue.offer(itemStack);
            currentStack.setStackInSlot(0, itemStack);
        }
    }
}
