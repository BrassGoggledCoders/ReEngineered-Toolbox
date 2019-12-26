package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public abstract class ConduitCore<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> implements INBTSerializable<CompoundNBT> {
    private final TYPE conduitType;
    private final ITextComponent name;

    private final Set<ConduitClient<CONTENT, CONTEXT, TYPE>> connectedClients;

    private UUID uuid;

    protected ConduitCore(TYPE conduitType, ITextComponent name) {
        this.conduitType = conduitType;
        this.connectedClients = Sets.newHashSet();
        this.name = name;
    }

    public TYPE getConduitType() {
        return conduitType;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uuid", this.uuid);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("uuid")) {
            this.uuid = nbt.getUniqueId("uuid");
        } else {
            this.uuid = UUID.randomUUID();
        }
    }

    public Set<ConduitClient<CONTENT, CONTEXT, TYPE>> getClients() {
        return connectedClients;
    }

    @Nonnull
    public abstract CONTENT request(CONTEXT context);

    @Nonnull
    public abstract CONTENT offer(CONTEXT context);

    public void addClient(ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient) {
        this.connectedClients.add(conduitClient);
    }

    public void removeClient(ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient) {
        this.connectedClients.remove(conduitClient);
    }

    public ITextComponent getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
