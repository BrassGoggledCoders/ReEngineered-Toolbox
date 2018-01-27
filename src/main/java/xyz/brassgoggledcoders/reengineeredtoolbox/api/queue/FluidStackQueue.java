package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.teamacronymcoders.base.util.FluidStackUtils;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class FluidStackQueue extends SocketQueue<FluidStack> {
    public FluidStackQueue() {
        super(5);
    }

    @Override
    public FluidStack simulateOffer(FluidStack value) {
        FluidStack remaining = value.copy();

        Optional<FluidStack> endOfQueue = this.getEndOfQueue();

        endOfQueue.ifPresent(fluidStack -> remaining.amount -= FluidStackUtils.moveFluid(value, fluidStack.copy(), 1000, 1000));

        if (remaining.amount > 0) {
            int amountOfSlots = remaining.amount / 1000;
            int remainingSlots = this.getQueueSize() - this.getLength();
            if (amountOfSlots > remainingSlots) {
                remaining.amount -= (remainingSlots * 1000);
            } else {
                remaining.amount = 0;
            }
        }

        return remaining;
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
}
