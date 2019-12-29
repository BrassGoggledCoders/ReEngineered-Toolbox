package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class ConduitCoreType<CORE extends ConduitCore<?, ?, TYPE>, TYPE extends ConduitType<?, ?, TYPE>>
        extends ForgeRegistryEntry<ConduitCoreType<?, ?>> {

    private final Supplier<TYPE> conduitType;
    private final Supplier<CORE> coreSupplier;

    public ConduitCoreType(Supplier<TYPE> conduitType, Supplier<CORE> coreSupplier) {
        this.conduitType = conduitType;
        this.coreSupplier = coreSupplier;
    }

    public TYPE getConduitType() {
        return conduitType.get();
    }

    public CORE createCore() {
        return this.coreSupplier.get();
    }
}
