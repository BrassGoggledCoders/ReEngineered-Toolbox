package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;

public class FrequencyBackedFluidHandler implements IFluidHandler {
    private final FrameSlot frameSlot;
    private final LazyOptional<IFrequencyFluidHandler> backingFluidHandler;
    private final IOStyle ioStyle;

    public FrequencyBackedFluidHandler(FrameSlot frameSlot, LazyOptional<IFrequencyFluidHandler> backingFluidHandler, IOStyle ioStyle) {
        this.frameSlot = frameSlot;
        this.backingFluidHandler = backingFluidHandler;
        this.ioStyle = ioStyle;
    }

    public FrequencyBackedFluidHandler(FrameSlot frameSlot, IOStyle ioStyle, ICapabilityProvider capabilityProvider) {
        this.frameSlot = frameSlot;
        this.backingFluidHandler = capabilityProvider.getCapability(ReEngineeredCapabilities.FREQUENCY_FLUID_HANDLER);
        this.ioStyle = ioStyle;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    @NotNull
    public FluidStack getFluidInTank(int tank) {
        if (tank == 0) {
            return this.backingFluidHandler.map(fluidHandler -> fluidHandler.getFluid(this.frameSlot.getFrequency()))
                    .orElse(FluidStack.EMPTY);
        } else {
            return FluidStack.EMPTY;
        }

    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank == 0) {
            return this.backingFluidHandler.map(fluidHandler -> fluidHandler.getCapacity(this.frameSlot.getFrequency()))
                    .orElse(0);
        } else {
            return 0;
        }
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.backingFluidHandler.map(fluidHandler -> fluidHandler.isFluidValid(this.frameSlot.getFrequency(), stack))
                .orElse(false);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (ioStyle.isAllowInsert()) {
            return this.backingFluidHandler.map(fluidHandler -> fluidHandler.fill(this.frameSlot.getFrequency(), resource, action))
                    .orElse(0);
        }
        return 0;
    }

    @Override
    @NotNull
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (ioStyle.isAllowExtract()) {
            return this.backingFluidHandler.map(fluidHandler -> fluidHandler.drain(this.frameSlot.getFrequency(), resource, action))
                    .orElse(FluidStack.EMPTY);
        }
        return FluidStack.EMPTY;
    }

    @Override
    @NotNull
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (ioStyle.isAllowExtract()) {
            return this.backingFluidHandler.map(fluidHandler -> fluidHandler.drain(this.frameSlot.getFrequency(), maxDrain, action))
                    .orElse(FluidStack.EMPTY);
        }
        return FluidStack.EMPTY;
    }
}
