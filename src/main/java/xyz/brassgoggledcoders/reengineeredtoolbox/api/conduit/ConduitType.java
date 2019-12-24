package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;

public abstract class ConduitType<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends ForgeRegistryEntry<ConduitType<?, ?, ?>> {
    private final IOType ioType;

    public ConduitType(IOType ioType) {
        this.ioType = ioType;
    }

    @SuppressWarnings("unchecked")
    public <T extends ConduitCore<CONTENT, CONTEXT, TYPE>> Optional<T> cast(ConduitCore<?, ?, ?> conduitCore) {
        if (this == conduitCore.getConduitType()) {
            return Optional.of((T) conduitCore);
        } else {
            return Optional.empty();
        }
    }

    public IOType getIoType() {
        return ioType;
    }

    public abstract ConduitCore<CONTENT, CONTEXT, TYPE> createCore();
}
