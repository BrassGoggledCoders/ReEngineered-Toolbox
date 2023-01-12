package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;

public class Connection<T extends ITypedSlot<U>, U> implements INBTSerializable<CompoundTag> {
    private final Class<T> tClass;
    private final ITypedSlotHolder slotHolder;
    private final Port port;

    private int slotId = -1;

    public Connection(Class<T> tClass, ITypedSlotHolder slotHolder, Port port) {
        this.tClass = tClass;
        this.slotHolder = slotHolder;
        this.port = port;
    }

    public void setSlotConnector(Port port, int slotId) {
        if (this.port.equals(port)) {
            if (this.matches(this.slotHolder.getSlot(slotId))) {
                this.slotId = slotId;
                T typedSlot = this.getConnectedSlot();
                if (typedSlot != null) {
                    this.afterConnection(this.getConnectedSlot());
                }
            }
        }
    }

    protected void afterConnection(T typedSlot) {

    }

    public T getConnectedSlot() {
        ITypedSlot<?> typedSlot = this.slotHolder.getSlot(slotId);
        if (this.matches(typedSlot)) {
            return tClass.cast(typedSlot);
        }
        return null;
    }

    public boolean isConnected() {
        return slotId >= 0 && this.matches(slotHolder.getSlot(slotId));
    }

    private boolean matches(ITypedSlot<?> typedSlot) {
        return tClass.isInstance(typedSlot);
    }

    public int getSlotId() {
        return slotId;
    }

    public boolean isConnectedTo(int slot) {
        return this.getSlotId() == slot;
    }

    public void clear() {

    }

    public Port getPort() {
        return port;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("SlotId", this.getSlotId());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setSlotConnector(this.port, nbt.getInt("SlotId"));
    }
}
