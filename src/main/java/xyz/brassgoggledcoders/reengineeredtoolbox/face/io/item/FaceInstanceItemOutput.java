package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.teamacronymcoders.base.capability.item.ItemStackHandlerExport;
import com.teamacronymcoders.base.capability.item.ItemStackHandlerImport;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class FaceInstanceItemOutput extends FaceInstance {
    private ItemStackHandlerExport currentStack = new ItemStackHandlerExport(1);
    private int itemQueueOutput = 0;

    @Override
    public void onTick(ISocketTile tile) {
        ItemStack itemStack = currentStack.getStackInSlot(0);
        if (itemStack.isEmpty()) {
            ItemStackQueue itemStackQueue = tile.getItemStackQueue(itemQueueOutput);
            itemStackQueue.pull().ifPresent(queueStack -> currentStack.setStackInSlot(0, queueStack));
        }
    }

    @Override
    public void configureQueue(String name, int queueNumber) {
        if ("itemQueueOutput".equals(name)) {
            itemQueueOutput = queueNumber;
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

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("itemQueueOutput", itemQueueOutput);
        tagCompound.setTag("itemHandler", currentStack.serializeNBT());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        itemQueueOutput = nbt.getInteger("itemQueueOutput");
        currentStack.deserializeNBT(nbt.getCompoundTag("itemHandler"));
    }
}
