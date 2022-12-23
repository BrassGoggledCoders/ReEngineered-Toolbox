package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FluidTypedSlotFluidHandler implements IFluidHandler {
    private final ITypedSlotHolder typedSlotHolder;

    public FluidTypedSlotFluidHandler(ITypedSlotHolder typedSlotHolder) {
        this.typedSlotHolder = typedSlotHolder;
    }

    @Override
    public int getTanks() {
        int tanks = 0;
        for (ITypedSlot<?> slot : this.typedSlotHolder.getSlots()) {
            if (slot instanceof IFluidTypedSlot) {
                tanks++;
            }
        }
        return tanks;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        IFluidTypedSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.getFluid();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        IFluidTypedSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.getCapacity();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        IFluidTypedSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.isFluidValid(stack);
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int filled = 0;
        int tank = 0;
        List<IFluidTypedSlot> fluidHypperSlots = this.getFluidHyperSlots();
        while (tank < fluidHypperSlots.size() && filled < resource.getAmount()) {
            FluidStack filling = resource;
            if (filled != 0) {
                filling = resource.copy();
                if (!filling.isEmpty()) {
                    filling.setAmount(resource.getAmount() - filled);
                }
            }
            filled += fluidHypperSlots.get(tank).fill(filling, action);
            tank++;
        }
        return filled;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack fluidStack = FluidStack.EMPTY;
        Iterator<IFluidTypedSlot> tankIterator = this.getFluidHyperSlots().iterator();
        while (tankIterator.hasNext() && fluidStack.getAmount() < resource.getAmount()) {
            IFluidTank nextTank = tankIterator.next();
            if (fluidStack.isEmpty()) {
                fluidStack = nextTank.drain(resource, action);
            } else if (resource.isFluidEqual(nextTank.getFluid())) {
                fluidStack.grow(nextTank.drain(resource.getAmount() - fluidStack.getAmount(), action).getAmount());
            }
        }
        return fluidStack;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack fluidStack = FluidStack.EMPTY;
        Iterator<IFluidTypedSlot> tankIterator = this.getFluidHyperSlots().iterator();
        while (tankIterator.hasNext() && fluidStack.getAmount() < maxDrain) {
            IFluidTank nextTank = tankIterator.next();
            if (fluidStack.isEmpty()) {
                fluidStack = nextTank.drain(maxDrain, action);
            } else if (fluidStack.isFluidEqual(nextTank.getFluid())) {
                fluidStack.grow(nextTank.drain(maxDrain - fluidStack.getAmount(), action).getAmount());
            }
        }
        return fluidStack;
    }

    private IFluidTypedSlot getFluidHyperSlot(int slotNum) {
        int tanks = 0;
        for (ITypedSlot<?> slot : this.typedSlotHolder.getSlots()) {
            if (slot instanceof IFluidTypedSlot fluidHypperSlot) {
                if (tanks == slotNum) {
                    return fluidHypperSlot;
                }
                tanks++;
            }
        }
        return null;
    }

    private List<IFluidTypedSlot> getFluidHyperSlots() {
        List<IFluidTypedSlot> fluidHypperSlots = new ArrayList<>();
        for (ITypedSlot<?> slot : this.typedSlotHolder.getSlots()) {
            if (slot instanceof IFluidTypedSlot fluidHypperSlot) {
                fluidHypperSlots.add(fluidHypperSlot);
            }
        }
        return fluidHypperSlots;
    }
}