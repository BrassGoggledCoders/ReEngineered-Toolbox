package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConduitManager implements IConduitManager {
    private int maxCores;
    private List<ConduitCore<?>> cores;

    public List<ConduitCore<?>> getCores() {
        return cores;
    }

    public <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE, CORE, CLIENT>,
            CORE extends ConduitCore<TYPE>, CLIENT extends ConduitClient<TYPE>> List<CORE> getCoresFor(
                    ConduitType<CONTENT, CONTEXT, TYPE, CORE, CLIENT> conduitType) {
        return cores.stream()
                .map(conduitType::cast)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public boolean addCore(ConduitCore<?> conduitCore) {
        if (cores.size() < maxCores) {
            return cores.add(conduitCore);
        } else {
            return false;
        }
    }
}
