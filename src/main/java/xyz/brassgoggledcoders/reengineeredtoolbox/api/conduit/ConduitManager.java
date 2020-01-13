package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitCore;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ConduitManager implements IConduitManager {
    private int maxCores;
    private Set<ConduitCore<?, ?, ?>> cores;

    public ConduitManager(int maxCores) {
        this.cores = Sets.newHashSet();
        this.maxCores = maxCores;
        RETObjects.REDSTONE_CORE_TYPE
                .map(ConduitCoreType::createCore)
                .ifPresent(this::addCore);
        RETObjects.ENERGY_CORE_TYPE
                .map(ConduitCoreType::createCore)
                .ifPresent(this::addCore);
    }

    @Override
    public int getMaxCores() {
        return maxCores;
    }

    @Override
    public Set<ConduitCore<?, ?, ?>> getAllCores() {
        return this.cores;
    }
}
