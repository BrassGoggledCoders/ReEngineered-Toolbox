package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

public interface IFrequencyRedstoneHandler {
    int getPower(Frequency frequency);

    <T> void addPowerProvider(T provider, BiConsumer<T, ObjIntConsumer<Frequency>> getPower);

    void markRequiresUpdate();
}
