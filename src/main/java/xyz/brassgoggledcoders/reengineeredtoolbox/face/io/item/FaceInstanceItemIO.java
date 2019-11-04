package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstanceItemIO extends FaceInstance {
    protected ItemStackHandler currentStack;
    protected int itemQueueNumber = 0;

    public FaceInstanceItemIO(ItemStackHandler itemStackHandler) {
        this.currentStack = itemStackHandler;
    }

    @Override
    public void configureQueue(String name, int queueNumber) {
        if ("itemQueueNumber".equals(name)) {
            itemQueueNumber = queueNumber;
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
    public CompoundNBT serializeNBT() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.putInt("itemQueueNumber", itemQueueNumber);
        tagCompound.put("itemHandler", currentStack.serializeNBT());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        itemQueueNumber = nbt.getInt("itemQueueNumber");
        currentStack.deserializeNBT(nbt.getCompound("itemHandler"));
    }

    @Override
    public boolean hasGui() {
        return true;
    }
}
