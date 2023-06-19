package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

public class FrequencyRedstoneHandler implements IFrequencyRedstoneHandler {
    private final IFrameEntity frame;
    private boolean needsUpdate;
    private final Object2IntMap<Frequency> powerCache;
    private final List<RedstoneProvider<?>> redstoneProviderList;

    public FrequencyRedstoneHandler(IFrameEntity frame) {
        this.frame = frame;
        this.needsUpdate = true;
        this.powerCache = new Object2IntArrayMap<>();
        this.redstoneProviderList = new ArrayList<>();
    }

    @Override
    public int getPower(Frequency frequency) {
        return this.powerCache.getOrDefault(frequency, 0);
    }

    @Override
    public <T> void addPowerProvider(T provider, BiConsumer<T, ObjIntConsumer<Frequency>> powerProviding) {
        this.redstoneProviderList.add(new RedstoneProvider<T>(provider, powerProviding));
    }

    @Override
    public void markRequiresUpdate() {
        this.needsUpdate = true;
    }

    public void tick() {
        if (this.needsUpdate) {
            this.powerCache.clear();
            this.redstoneProviderList.removeIf(Predicate.not(RedstoneProvider::isValid));
            this.redstoneProviderList.forEach(redstoneProvider -> redstoneProvider.providePower((frequency, power) ->
                    this.powerCache.merge(
                            frequency,
                            power,
                            Math::max
                    )
            ));
            this.frame.notifyStorageChange(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
            this.needsUpdate = false;
        }
    }
}
