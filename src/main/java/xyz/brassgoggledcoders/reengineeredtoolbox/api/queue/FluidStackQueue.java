package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class FluidStackQueue extends SocketQueue<FluidStack> {
    public FluidStackQueue() {
        super(5);
    }

    @Override
    protected FluidStack addToBack(FluidStack value) {
        Optional<FluidStack> endOfQueue = this.getEndOfQueue();
        endOfQueue.ifPresent(fluidStack -> moveFluid(value, fluidStack, 1000, 1000));
        if (anyRemainingValue(value) && this.getLength() < this.getQueueSize()) {
            FluidStack newStack = value.copy();
            newStack.setAmount(0);
            moveFluid(value, newStack, 1000, 1000);
            this.push(newStack);
        }
        return value;
    }

    @Override
    protected boolean anyRemainingValue(FluidStack value) {
        return value.getFluid() != null && value.getAmount() > 0;
    }

    @Override
    public CompoundNBT serializeValue(FluidStack value) {
        return value.writeToNBT(new CompoundNBT());
    }

    @Override
    public FluidStack deserializeValue(CompoundNBT compoundNBT) {
        return FluidStack.loadFluidStackFromNBT(compoundNBT);
    }

    private static int moveFluid(FluidStack from, FluidStack to, int amount, int maxToAmount) {
        int amountMoved = 0;
        if (from.isFluidEqual(to)) {
            amountMoved = amount;
            if (amountMoved > from.getAmount()) {
                amountMoved = from.getAmount();
            }

            int amountToCanAccept = maxToAmount - to.getAmount();
            if (amountToCanAccept <= 0) {
                amountMoved = 0;
            } else if (amountMoved > amountToCanAccept) {
                amountMoved = amountToCanAccept;
            }

            to.grow(amountMoved);
            from.shrink(amountMoved);
        }
        return amountMoved;
    }
}
