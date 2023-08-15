package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.collector.CompoundTagCollector;

import java.util.Map;

public class FrequencyFluidHandler implements IFrequencyFluidHandler, INBTSerializable<CompoundTag> {
    private final Map<Frequency, FluidTank> fluidTanks;
    private final Runnable onChange;

    public FrequencyFluidHandler(Runnable onChange) {
        this.onChange = onChange;
        this.fluidTanks = Maps.newEnumMap(Frequency.class);
    }

    @Override
    @NotNull
    public FluidStack getFluid(Frequency frequency) {
        return this.getFluidTank(frequency).getFluid();
    }

    @Override
    public int getCapacity(Frequency frequency) {
        return this.getFluidTank(frequency).getCapacity();
    }

    @Override
    public boolean hasCapacity(Frequency frequency) {
        IFluidTank fluidTank = this.getFluidTank(frequency);
        return fluidTank.getFluidAmount() < fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(Frequency frequency, @NotNull FluidStack fluidStack) {
        return this.getFluidTank(frequency).isFluidValid(fluidStack);
    }

    @Override
    public int fill(Frequency frequency, FluidStack fluidStack, FluidAction fluidAction) {
        int filled = this.getFluidTank(frequency).fill(fluidStack, fluidAction);
        if (filled > 0 && fluidAction.execute()) {
            this.onContentsChanged();
        }
        return filled;
    }

    @Override
    @NotNull
    public FluidStack drain(Frequency frequency, FluidStack resource, FluidAction action) {
        FluidStack drained = this.getFluidTank(frequency).drain(resource, action);
        if (!drained.isEmpty() && action.execute()) {
            this.onContentsChanged();
        }
        return drained;
    }

    @Override
    @NotNull
    public FluidStack drain(Frequency frequency, int maxDrain, FluidAction action) {
        FluidStack drained = this.getFluidTank(frequency).drain(maxDrain, action);
        if (!drained.isEmpty() && action.execute()) {
            this.onContentsChanged();
        }
        return drained;
    }

    private void onContentsChanged() {
        this.onChange.run();
    }

    private IFluidTank getFluidTank(Frequency frequency) {
        return this.fluidTanks.computeIfAbsent(frequency, value -> new FluidTank(FluidType.BUCKET_VOLUME * 8));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("FluidTanks", fluidTanks.entrySet()
                .stream()
                .filter(fluidTank -> !fluidTank.getValue().isEmpty())
                .collect(CompoundTagCollector.forEntry(Frequency::name, fluidTank -> fluidTank.writeToNBT(new CompoundTag())))
        );
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag fluidTanksTag = nbt.getCompound("FluidTanks");
        this.fluidTanks.clear();
        for (String key : fluidTanksTag.getAllKeys()) {
            Frequency.getByName(key)
                    .ifPresent(frequency -> {
                        FluidTank fluidTank = new FluidTank(FluidType.BUCKET_VOLUME * 8);
                        fluidTank.readFromNBT(fluidTanksTag.getCompound(key));
                        if (!fluidTank.isEmpty()) {
                            this.fluidTanks.put(frequency, fluidTank);
                        }
                    });
        }
    }
}
