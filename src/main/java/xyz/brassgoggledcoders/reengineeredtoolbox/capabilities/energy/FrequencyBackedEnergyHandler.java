package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;

public class FrequencyBackedEnergyHandler implements IEnergyStorage {
    private final FrameSlot frameSlot;
    private final IOStyle ioStyle;
    private final Option<IFrequencyEnergyHandler> backingEnergyStorage;

    public FrequencyBackedEnergyHandler(FrameSlot frameSlot, LazyOptional<IFrequencyEnergyHandler> backingEnergyStorage, IOStyle ioStyle) {
        this.frameSlot = frameSlot;
        this.ioStyle = ioStyle;
        this.backingEnergyStorage = Option.ofLazy(backingEnergyStorage);
    }

    public FrequencyBackedEnergyHandler(FrameSlot frameSlot, IOStyle ioStyle, ICapabilityProvider provider) {
        this(frameSlot, provider.getCapability(ReEngineeredCapabilities.FREQUENCY_ENERGY_HANDLER), ioStyle);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (this.ioStyle.isAllowInsert()) {
            return this.backingEnergyStorage.map(energyStorage -> energyStorage.receiveEnergy(frameSlot.getFrequency(), maxReceive, simulate))
                    .orElse(0);
        } else {
            return 0;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (this.ioStyle.isAllowExtract()) {
            return this.backingEnergyStorage.map(energyStorage -> energyStorage.extractEnergy(frameSlot.getFrequency(), maxExtract, simulate))
                    .orElse(0);
        } else {
            return 0;
        }
    }

    @Override
    public int getEnergyStored() {
        return this.backingEnergyStorage.map(energyStorage -> energyStorage.getEnergyStored(frameSlot.getFrequency()))
                .orElse(0);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.backingEnergyStorage.map(energyStorage -> energyStorage.getMaxEnergyStored(frameSlot.getFrequency()))
                .orElse(0);
    }

    @Override
    public boolean canExtract() {
        return this.ioStyle.isAllowExtract() && this.backingEnergyStorage.exists(energyStorage -> energyStorage.canExtract(frameSlot.getFrequency()));
    }

    @Override
    public boolean canReceive() {
        return this.ioStyle.isAllowInsert() && this.backingEnergyStorage.exists(energyStorage -> energyStorage.canReceive(frameSlot.getFrequency()));
    }

    public ProgressView getView() {
        return new ProgressView(
                this.getEnergyStored(),
                this.getMaxEnergyStored()
        );
    }
}
