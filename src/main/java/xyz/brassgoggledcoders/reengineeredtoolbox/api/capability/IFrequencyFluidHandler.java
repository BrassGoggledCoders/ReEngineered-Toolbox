package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

public interface IFrequencyFluidHandler {
    @NotNull
    FluidStack getFluid(Frequency frequency);

    int getCapacity(Frequency frequency);

    boolean hasCapacity(Frequency frequency);

    boolean isFluidValid(Frequency frequency, @NotNull FluidStack fluidStack);

    int fill(Frequency frequency, FluidStack fluidStack, FluidAction fluidAction);

    @NotNull
    FluidStack drain(Frequency frequency, FluidStack resource, FluidAction action);

    @NotNull
    FluidStack drain(Frequency frequency, int maxDrain, FluidAction action);
}
