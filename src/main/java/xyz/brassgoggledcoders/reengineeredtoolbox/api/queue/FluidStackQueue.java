package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.teamacronymcoders.base.util.FluidStackUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class FluidStackQueue extends SocketQueue<FluidStack> {
    public FluidStackQueue() {
        super(5);
    }

    @Override
    protected FluidStack addToBack(FluidStack value) {
        Optional<FluidStack> endOfQueue = this.getEndOfQueue();
        endOfQueue.ifPresent(fluidStack -> FluidStackUtils.moveFluid(value, fluidStack, 1000, 1000));
        if (anyRemainingValue(value) && this.getLength() < this.getQueueSize()) {
            FluidStack newStack = value.copy();
            newStack.amount = 0;
            FluidStackUtils.moveFluid(value, newStack, 1000, 1000);
            this.push(newStack);
        }
        return value;
    }

    @Override
    protected boolean anyRemainingValue(FluidStack value) {
        return value.getFluid() != null && value.amount > 0;
    }

    @Override
    public NBTTagCompound serializeValue(FluidStack value) {
        return value.writeToNBT(new NBTTagCompound());
    }

    @Override
    public FluidStack deserializeValue(NBTTagCompound nbtTagCompound) {
        return FluidStack.loadFluidStackFromNBT(nbtTagCompound);
    }
}
