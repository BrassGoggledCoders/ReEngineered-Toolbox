package xyz.brassgoggledcoders.reengineeredtoolbox.component.energy;

import net.minecraft.util.IntReferenceHolder;

public class EnergyStorageReferenceHolder extends IntReferenceHolder {
    private final PosEnergyStorage energyStorage;

    public EnergyStorageReferenceHolder(PosEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int get() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public void set(int value) {
        energyStorage.setEnergyStored(value);
    }
}
