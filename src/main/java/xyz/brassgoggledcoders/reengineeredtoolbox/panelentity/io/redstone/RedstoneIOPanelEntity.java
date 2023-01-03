package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;

import java.util.List;

public abstract class RedstoneIOPanelEntity extends PanelEntity {
    private int power;
    private int connectedSlotId = -1;

    public RedstoneIOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    public void setPortConnection(Port port, int slotNumber) {
        if (port.identifier().equals("redstone")) {
            ITypedSlotHolder typedSlotHolder = this.getFrameEntity()
                    .getTypedSlotHolder();
            if (typedSlotHolder.getSlot(slotNumber) instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                this.setConnectedSlotId(slotNumber);
                afterConnection(redstoneTypedSlot);
            }
        }
    }

    protected void afterConnection(IRedstoneTypedSlot typedSlot) {

    }

    public int getConnectedSlotId() {
        return connectedSlotId;
    }

    public void setConnectedSlotId(int connectedSlotId) {
        this.connectedSlotId = connectedSlotId;
    }

    @Nullable
    public IRedstoneTypedSlot getConnectedSlot() {
        ITypedSlot<?> slot = this.getFrameEntity()
                .getTypedSlotHolder()
                .getSlot(this.getConnectedSlotId());

        if (slot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
            return redstoneTypedSlot;
        } else {
            return null;
        }
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public abstract IRedstoneTypedSlot getSlotForMenu();

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.connectedSlotId = pTag.getInt("ConnectedSlotId");
        this.power = pTag.getInt("Power");
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.putInt("ConnectedSlotId", this.connectedSlotId);
        pTag.putInt("Power", this.power);
    }

    @Override
    public List<Port> getPorts() {
        return List.of(
                new Port("redstone", this.getPanelState().getPanel().getName(), this.getConnectedSlotId(), TypedSlotTypes.REDSTONE.get())
        );
    }

    public int getSignal() {
        return 0;
    }
}
