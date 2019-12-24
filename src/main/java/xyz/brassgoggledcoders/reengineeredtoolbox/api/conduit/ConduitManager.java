package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ConduitManager implements IConduitManager {
    private int maxCores;
    private Set<ConduitCore<?, ?, ?>> cores;

    public ConduitManager(int maxCores) {
        this.cores = Sets.newHashSet();
        this.maxCores = maxCores;
    }

    public Set<ConduitCore<?, ?, ?>> getCores() {
        return cores;
    }

    public <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            ConduitType<CONTENT, CONTEXT, TYPE> conduitType) {
        return cores.stream()
                .map(conduitType::cast)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public boolean addCore(ConduitCore<?, ?, ?> conduitCore) {
        if (cores.size() < maxCores) {
            return cores.add(conduitCore);
        } else {
            return false;
        }
    }
}
