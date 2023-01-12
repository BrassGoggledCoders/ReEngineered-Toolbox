package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class FreezerRecipeContainer extends RecipeWrapper implements IFluidHandler {

    private final IFluidHandler fluidHandler;

    public FreezerRecipeContainer(IItemHandlerModifiable inv, IFluidHandler fluidHandler) {
        super(inv);
        this.fluidHandler = fluidHandler;
    }

    public int getTanks() {
        return fluidHandler.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidHandler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidHandler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidHandler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidHandler.fill(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return fluidHandler.drain(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return fluidHandler.drain(maxDrain, action);
    }
}
