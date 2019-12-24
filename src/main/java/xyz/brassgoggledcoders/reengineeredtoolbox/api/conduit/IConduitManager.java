package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import java.util.Set;

public interface IConduitManager {
    <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            ConduitType<CONTENT, CONTEXT, TYPE> conduitType);

    boolean addCore(ConduitCore<?, ?, ?> conduitCore);
}
