package xyz.brassgoggledcoders.reengineeredtoolbox.component.fluid;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import net.minecraftforge.fluids.FluidStack;

public class ExtendedFluidTank extends PosFluidTank {
    public ExtendedFluidTank(String name, int amount, int posX, int posY) {
        super(name, amount, posX, posY);
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluid = fluidStack;
    }

    public FluidReferenceHolder getReferenceHolder() {
        return new FluidReferenceHolder(this);
    }
}
