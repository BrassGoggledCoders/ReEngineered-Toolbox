package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ConduitType<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends ForgeRegistryEntry<ConduitType<?, ?, ?>> implements Supplier<ConduitType<CONTENT, CONTEXT, TYPE>> {
    private final Supplier<? extends ConduitCoreType<?, TYPE>> defaultCoreTypeSupplier;
    private final Supplier<CONTENT> emptySupplier;

    protected ConduitType(Supplier<? extends ConduitCoreType<?, TYPE>> defaultCoreTypeSupplier, Supplier<CONTENT> emptySupplier) {
        this.defaultCoreTypeSupplier = defaultCoreTypeSupplier;
        this.emptySupplier = emptySupplier;
    }

    @SuppressWarnings("unchecked")
    public <CORE extends ConduitCore<CONTENT, CONTEXT, TYPE>> Optional<CORE> cast(ConduitCore<?, ?, ?> conduitCore) {
        if (this == conduitCore.getConduitType()) {
            return Optional.of((CORE) conduitCore);
        } else {
            return Optional.empty();
        }
    }

    protected ConduitCoreType<?, TYPE> getDefaultCoreType() {
        return defaultCoreTypeSupplier.get();
    }

    protected Supplier<CONTENT> emptySupplier() {
        return emptySupplier;
    }

    public ConduitCore<CONTENT, CONTEXT, TYPE> createEmptyCore() {
        return new EmptyCore<>(this.getDefaultCoreType().getConduitType(), this.emptySupplier());
    }

    @Override
    public ConduitType<CONTENT, CONTEXT, TYPE> get() {
        return this;
    }
}
