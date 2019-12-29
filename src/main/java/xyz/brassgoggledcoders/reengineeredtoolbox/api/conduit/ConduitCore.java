package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public abstract class ConduitCore<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        implements INBTSerializable<CompoundNBT> {

    private final TYPE conduitType;
    private final ConduitCoreType<?, TYPE> conduitCoreType;
    private final Set<ConduitClient<CONTENT, CONTEXT, TYPE>> connectedClients;

    private ITextComponent name;
    private String translationKey;
    private UUID uuid;

    protected ConduitCore(TYPE conduitType, ConduitCoreType<?, TYPE> conduitCoreType) {
        this.conduitType = conduitType;
        this.conduitCoreType = conduitCoreType;
        this.connectedClients = Sets.newHashSet();
        this.uuid = UUID.randomUUID();
    }

    public TYPE getConduitType() {
        return conduitType;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

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

    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    public ITextComponent getName() {
        if (this.name == null) {
            this.name = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.name;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("conduit_core", this.getConduitCoreType().getRegistryName());
        }
        return this.translationKey;
    }

    public ConduitCoreType<?, TYPE> getConduitCoreType() {
        return conduitCoreType;
    }

    public boolean isEmpty() {
        return false;
    }
}
