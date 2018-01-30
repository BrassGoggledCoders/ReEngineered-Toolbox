package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.teamacronymcoders.base.capability.item.ItemStackHandlerExport;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

public class FaceInstanceItemOutput extends FaceInstanceItemIO {
    public FaceInstanceItemOutput() {
        super(new ItemStackHandlerExport());
    }

    @Override
    public void onTick(ISocketTile tile) {
        ItemStack itemStack = currentStack.getStackInSlot(0);
        if (itemStack.isEmpty()) {
            ItemStackQueue itemStackQueue = tile.getItemStackQueue(itemQueueNumber);
            itemStackQueue.pull().ifPresent(queueStack -> currentStack.setStackInSlot(0, queueStack));
        }
    }
}
