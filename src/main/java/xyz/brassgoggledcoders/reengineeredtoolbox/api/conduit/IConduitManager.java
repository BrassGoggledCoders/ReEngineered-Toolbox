package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface IConduitManager {
    default <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            Supplier<? extends ConduitType<CONTENT, CONTEXT, TYPE>> conduitType) {
        return getCoresFor(conduitType.get());
    }

    default <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            @Nullable ConduitType<CONTENT, CONTEXT, TYPE> conduitType) {
        return Optional.ofNullable(conduitType)
                .map(value -> this.getAllCores().stream()
                        .map(value::cast)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet())
                )
                .orElseGet(Sets::newHashSet);
    }

    default boolean addCore(ConduitCore<?, ?, ?> conduitCore) {
        if (this.getAllCores().size() < this.getMaxCores()) {
            return this.getAllCores().add(conduitCore);
        } else {
            return false;
        }
    }

    int getMaxCores();

    Set<ConduitCore<?, ?, ?>> getAllCores();
}
