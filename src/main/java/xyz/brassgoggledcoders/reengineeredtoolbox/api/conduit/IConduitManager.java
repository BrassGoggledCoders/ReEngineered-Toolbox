package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import java.util.Set;
import java.util.function.Supplier;

public interface IConduitManager {
    default <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            Supplier<? extends ConduitType<CONTENT, CONTEXT, TYPE>> conduitType) {
        return getCoresFor(conduitType.get());
    }

    <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            ConduitType<CONTENT, CONTEXT, TYPE> conduitType);

    boolean addCore(ConduitCore<?, ?, ?> conduitCore);
}
