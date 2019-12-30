package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.UUID;

public abstract class ConduitClient<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        implements INBTSerializable<CompoundNBT> {
    private final TYPE conduitType;
    private final ITextComponent name;

    private ConduitCore<CONTENT, CONTEXT, TYPE> connectedCore;
    private UUID uuid;

    protected ConduitClient(TYPE conduitType, @Nonnull ITextComponent name) {
        this.conduitType = conduitType;
        this.connectedCore = conduitType.createEmptyCore();
        this.name = name;
        this.uuid = UUID.randomUUID();
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
        nbt.putUniqueId("uuid", this.getUuid());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasUniqueId("uuid")) {
            this.setUuid(nbt.getUniqueId("uuid"));
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void tryConnect(IConduitManager manager, UUID uuid) {
        manager.getCoreByUUID(uuid)
                .flatMap(this.getConduitType()::cast)
                .ifPresent(this::setConnectedCore);
    }
}
