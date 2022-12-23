package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;

public interface IFluidTypedSlot extends ITypedSlot<FluidStack>, IFluidTank {

    @Override
    default @NotNull TypedSlotType getType() {
        return null;
    }

    @Override
    default FluidStack getContent() {
        return this.getFluid();
    }
}
