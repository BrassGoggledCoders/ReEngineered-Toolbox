package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

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
    @SuppressWarnings("unchecked")
    public <T> void addPowerProvider(T provider, BiConsumer<T, ObjIntConsumer<Frequency>> powerProviding) {
        if (provider instanceof PanelEntity panelEntity) {
            this.redstoneProviderList.add(new RedstoneProvider<>(
                    panelEntity,
                    Predicate.not(PanelEntity::isRemoved),
                    (BiConsumer<PanelEntity, ObjIntConsumer<Frequency>>) powerProviding)
            );
        } else {
            this.redstoneProviderList.add(new RedstoneProvider<>(provider, powerProviding));
        }
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
