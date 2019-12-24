package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class EmptyCore<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends ConduitCore<CONTENT, CONTEXT, TYPE> {

    private final Supplier<CONTENT> emptySupplier;

    public EmptyCore(TYPE conduitType, Supplier<CONTENT> emptySupplier) {
        super(conduitType);
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
}
