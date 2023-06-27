package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

public interface IFrequencyEnergyHandler {
    int receiveEnergy(Frequency frequency, int maxReceive, boolean simulate);

    int extractEnergy(Frequency frequency, int maxExtract, boolean simulate);

    int getEnergyStored(Frequency frequency);

    int getMaxEnergyStored(Frequency frequency);

    boolean canExtract(Frequency frequency);

    boolean canReceive(Frequency frequency);
}
