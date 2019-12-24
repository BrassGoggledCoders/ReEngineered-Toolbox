package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ConduitCore<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> implements INBTSerializable<CompoundNBT> {
    private final TYPE conduitType;

    private final List<ConduitClient<CONTENT, CONTEXT, TYPE>> connectedClients;

    protected ConduitCore(TYPE conduitType) {
        this.conduitType = conduitType;
        connectedClients = Lists.newArrayList();
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

    public List<ConduitClient<CONTENT, CONTEXT, TYPE>> getClients() {
        return connectedClients;
    }

    @Nonnull
    public abstract CONTENT request(CONTEXT context);

    @Nonnull
    public abstract CONTENT offer(CONTEXT context);
}
