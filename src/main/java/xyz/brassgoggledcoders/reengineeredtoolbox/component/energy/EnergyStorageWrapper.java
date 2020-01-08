package xyz.brassgoggledcoders.reengineeredtoolbox.component.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageWrapper implements IEnergyStorage {
    private final boolean allowExtract;
    private final boolean allowReceive;
    private final IEnergyStorage wrappedStorage;

    public EnergyStorageWrapper(boolean allowExtract, boolean allowReceive, IEnergyStorage wrappedStorage) {
        this.allowExtract = allowExtract;
        this.allowReceive = allowReceive;
        this.wrappedStorage = wrappedStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return allowReceive ? wrappedStorage.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return allowExtract ? wrappedStorage.extractEnergy(maxExtract, simulate) : 0;

    }

    @Override
    public int getEnergyStored() {
        return wrappedStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return wrappedStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return allowExtract && wrappedStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return allowReceive && wrappedStorage.canReceive();
    }
}
