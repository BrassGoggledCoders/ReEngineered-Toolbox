package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.collector.CompoundTagCollector;

import java.util.Map;

public class FrequencyEnergyHandler implements IFrequencyEnergyHandler, INBTSerializable<CompoundTag> {
    private final int maxEnergy;
    private final Runnable onChange;
    private final Map<Frequency, EnergyStorage> energyStorages;

    public FrequencyEnergyHandler(int maxEnergy, Runnable onChange) {
        this.maxEnergy = maxEnergy;
        this.onChange = onChange;
        energyStorages = Maps.newEnumMap(Frequency.class);
    }

    @Override
    public int receiveEnergy(Frequency frequency, int maxReceive, boolean simulate) {
        int energyReceived = this.getEnergyStorage(frequency)
                .receiveEnergy(maxReceive, simulate);

        if (energyReceived > 0 && !simulate) {
            this.onContentsChanged();
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(Frequency frequency, int maxExtract, boolean simulate) {
        int energyExtracted = this.getEnergyStorage(frequency)
                .extractEnergy(maxExtract, simulate);

        if (energyExtracted > 0 && !simulate) {
            this.onContentsChanged();
        }

        return energyExtracted;
    }

    @Override
    public int getEnergyStored(Frequency frequency) {
        return this.getEnergyStorage(frequency)
                .getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(Frequency frequency) {
        return this.getEnergyStorage(frequency)
                .getMaxEnergyStored();
    }

    @Override
    public boolean canExtract(Frequency frequency) {
        return this.getEnergyStorage(frequency)
                .canExtract();
    }

    @Override
    public boolean canReceive(Frequency frequency) {
        return this.getEnergyStorage(frequency)
                .canReceive();
    }

    private EnergyStorage getEnergyStorage(Frequency frequency) {
        return this.energyStorages.computeIfAbsent(frequency, value -> new EnergyStorage(this.maxEnergy));
    }

    private void onContentsChanged() {
        this.onChange.run();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("EnergyStorages", energyStorages.entrySet()
                .stream()
                .filter(energyStorage -> energyStorage.getValue().getEnergyStored() > 0)
                .collect(CompoundTagCollector.forEntry(Frequency::name, EnergyStorage::serializeNBT))
        );
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag energyStorageTag = nbt.getCompound("EnergyStorage");
        this.energyStorages.clear();
        for (String key : energyStorageTag.getAllKeys()) {
            Frequency.getByName(key)
                    .ifPresent(frequency -> {
                        EnergyStorage energyStorage = new EnergyStorage(this.maxEnergy);
                        energyStorage.deserializeNBT(energyStorageTag.getCompound(key));
                        if (energyStorage.getEnergyStored() > 0) {
                            this.energyStorages.put(frequency, energyStorage);
                        }
                    });
        }
    }

}
