package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public abstract class ConduitClient<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        implements INBTSerializable<CompoundNBT> {
    private final TYPE conduitType;
    private final ITextComponent name;
    private final FaceInstance faceInstance;

    private ConduitCore<CONTENT, CONTEXT, TYPE> connectedCore;

    protected ConduitClient(TYPE conduitType, FaceInstance faceInstance, @Nonnull ITextComponent name) {
        this.conduitType = Optional.ofNullable(conduitType)
                .orElseThrow(() -> new IllegalStateException("Cannot have null Conduit Type"));
        this.connectedCore = this.conduitType.createEmptyCore();
        this.name = name;
        this.faceInstance = faceInstance;
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
        this.connectedCore = this.getConduitType().createEmptyCore();
    }

    public ITextComponent getName() {
        return name;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (!connectedCore.isEmpty()) {
            nbt.putUniqueId("coreUuid", connectedCore.getUuid());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasUniqueId("coreUuid")) {
            this.tryConnect(nbt.getUniqueId("coreUuid"));
        }
    }

    public void tryConnect(UUID uuid) {
        faceInstance.getSocket()
                .getConduitManager()
                .getCoreByUUID(uuid)
                .flatMap(this.getConduitType()::cast)
                .ifPresent(this::setConnectedCore);
    }
}
