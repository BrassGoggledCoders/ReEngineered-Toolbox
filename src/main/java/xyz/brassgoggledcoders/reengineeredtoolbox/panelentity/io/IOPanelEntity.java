package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;

import java.util.List;
import java.util.Optional;

public abstract class IOPanelEntity<T extends ITypedSlot<U>, U> extends PanelEntity {
    private int connectedSlotId = -1;

    public IOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    protected abstract Optional<T> getTypedSlot(ITypedSlot<?> typedSlot);

    @NotNull
    protected abstract String getIdentifier();

    @Override
    public void setPortConnection(Port port, int slotNumber) {
        if (port.identifier().equals(this.getIdentifier())) {
            ITypedSlotHolder typedSlotHolder = this.getFrameEntity()
                    .getTypedSlotHolder();

            this.getTypedSlot(typedSlotHolder.getSlot(slotNumber))
                    .ifPresent(typedSlot -> {
                        this.setConnectedSlotId(slotNumber);
                        afterConnection(typedSlot);
                    });
        }
    }

    public int getConnectedSlotId() {
        return connectedSlotId;
    }

    public void setConnectedSlotId(int connectedSlotId) {
        this.connectedSlotId = connectedSlotId;
    }

    protected void afterConnection(T typedSlot) {

    }

    @Nullable
    public T getConnectedSlot() {
        if (this.getConnectedSlotId() >= 0) {
            ITypedSlot<?> slot = this.getFrameEntity()
                    .getTypedSlotHolder()
                    .getSlot(this.getConnectedSlotId());

            return this.getTypedSlot(slot)
                    .orElse(null);
        }

        return null;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.connectedSlotId = pTag.getInt("ConnectedSlotId");
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.putInt("ConnectedSlotId", this.connectedSlotId);
    }

    @Override
    public List<Port> getPorts() {
        return List.of(new Port(
                this.getIdentifier(),
                this.getPanelState().getPanel().getName(),
                this.getConnectedSlotId(),
                this.getTypedSlotType()
        ));
    }

    public boolean isConnected() {
        return this.getConnectedSlotId() >= 0;
    }

    public abstract T getSlotForMenu();

    public abstract MenuConstructor getMenuCreator();

    public abstract TypedSlotType getTypedSlotType();
}
