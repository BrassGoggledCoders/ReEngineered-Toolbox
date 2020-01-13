package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class EmptyConduitCore<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends ConduitCore<CONTENT, CONTEXT, TYPE> {

    private final Supplier<CONTENT> emptySupplier;

    public EmptyConduitCore(TYPE conduitType, Supplier<CONTENT> emptySupplier) {
        super(conduitType, conduitType.getDefaultCoreType());
        this.emptySupplier = emptySupplier;
    }

    @Nonnull
    @Override
    public CONTENT request(CONTEXT context) {
        return emptySupplier.get();
    }

    @Nonnull
    @Override
    public CONTENT offer(CONTEXT context) {
        return emptySupplier.get();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
