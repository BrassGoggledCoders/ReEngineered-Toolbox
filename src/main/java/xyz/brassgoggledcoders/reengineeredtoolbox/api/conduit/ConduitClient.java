package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class ConduitClient<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> {
    private final TYPE conduitType;

    private ConduitCore<CONTENT, CONTEXT, TYPE> connectedCore;

    protected ConduitClient(TYPE conduitType) {
        this.conduitType = conduitType;
        this.connectedCore = new EmptyCore<>(conduitType, this.emptySupplier());
    }

    public TYPE getConduitType() {
        return conduitType;
    }

    public CONTENT request(CONTEXT context) {
        return this.getConnectedCore().request(context);
    }

    public CONTENT offer(CONTEXT context) {
        return this.getConnectedCore().offer(context);
    }
    
    public abstract CONTENT extractFrom(CONTEXT context);
    
    public abstract CONTENT insertInto(CONTEXT context);

    @Nonnull
    public ConduitCore<CONTENT, CONTEXT, TYPE> getConnectedCore() {
        return connectedCore;
    }

    public void setConnectedCore(@Nonnull ConduitCore<CONTENT, CONTEXT, TYPE> connectedCore) {
        this.connectedCore = connectedCore;
        connectedCore.addClient(this);
    }

    public void disconnectFromCore() {
        this.connectedCore.removeClient(this);
        this.connectedCore = new EmptyCore<>(conduitType, this.emptySupplier());
    }

    protected abstract Supplier<CONTENT> emptySupplier();
}
