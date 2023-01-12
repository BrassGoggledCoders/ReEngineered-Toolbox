package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Connection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;

import java.util.Map;

public abstract class IOPanelEntity<T extends ITypedSlot<U>, U, V extends Connection<T, U>> extends PanelEntity {

    private final Port ioPort;
    private V connection;

    public IOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.ioPort = new Port(
                this.getIdentifier(),
                this.getPanelState().getPanel().getName(),
                this.getTypedSlotType()
        );
        this.connection = this.createConnection();
    }

    @NotNull
    protected abstract String getIdentifier();

    protected abstract V createConnection();

    public V getConnection() {
        return this.connection;
    }

    @Override
    public void setPortConnection(Port port, int slotNumber) {
        this.getConnection().setSlotConnector(port, slotNumber);
    }

    @Nullable
    public T getConnectedSlot() {
        return this.getConnection().getConnectedSlot();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.connection.deserializeNBT(pTag.getCompound("Connection"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("Connection", this.connection.serializeNBT());
    }

    public Port getPort() {
        return this.ioPort;
    }

    @Override
    public Map<Port, Integer> getPorts() {
        return Map.of(ioPort, this.getConnection().getSlotId());
    }

    public abstract T getSlotForMenu();

    public abstract MenuConstructor getMenuCreator();

    public abstract TypedSlotType getTypedSlotType();
}
