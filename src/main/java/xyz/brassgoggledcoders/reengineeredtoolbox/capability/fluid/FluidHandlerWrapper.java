package xyz.brassgoggledcoders.reengineeredtoolbox.capability.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class FluidHandlerWrapper implements IFluidHandler {
    private final IFluidHandler fluidHandler;
    private final boolean canFill;
    private final boolean canDrain;

    public FluidHandlerWrapper(IFluidHandler fluidHandler, boolean canFill, boolean canDrain) {
        this.fluidHandler = fluidHandler;
        this.canFill = canFill;
        this.canDrain = canDrain;
    }

    @Override
    public int getTanks() {
        return fluidHandler.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidHandler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidHandler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return fluidHandler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (canFill) {
            return fluidHandler.fill(resource, action);
        }
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (canDrain) {
            return fluidHandler.drain(resource, action);
        }
        return FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (canDrain) {
            return fluidHandler.drain(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }
}
