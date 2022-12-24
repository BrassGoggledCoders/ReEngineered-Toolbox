package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSupplier;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneTypedSlot;

import java.util.UUID;

public class RedstoneInputPanelEntity extends PanelEntity {
    private int power;
    private int connectedSlot = -1;
    private RedstoneSupplier redstoneSupplier;

    public RedstoneInputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.REDSTONE_INPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneInputPanelEntity(@NotNull PanelEntityType<RedstoneInputPanelEntity> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    public boolean trySetSlotConnection(String identifier, int slotNumber) {
        if (identifier.equals("redstone")) {
            ITypedSlotHolder typedSlotHolder = this.getFrameEntity()
                    .getTypedSlotHolder();
            if (!(typedSlotHolder.getSlot(slotNumber) instanceof IRedstoneTypedSlot)) {
                typedSlotHolder.setSlot(slotNumber, new RedstoneTypedSlot());
            }
            if (typedSlotHolder.getSlot(slotNumber) instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                this.connectedSlot = slotNumber;
                this.redstoneSupplier = new RedstoneSupplier(
                        this::getPower,
                        this::checkValid,
                        UUID.randomUUID()
                );
                redstoneTypedSlot.setContent(this.redstoneSupplier);
            }
            return true;
        }
        return false;
    }

    @Override
    public void neighborChanged() {
        Direction direction = this.getFacing();
        if (direction != null) {
            int newPower = this.getLevel().getSignal(this.getBlockPos().relative(direction), direction);
            if (this.power != newPower) {
                this.power = newPower;
                if (connectedSlot >= 0) {
                    ITypedSlot<?> typedSlot = this.getFrameEntity()
                            .getTypedSlotHolder()
                            .getSlot(connectedSlot);

                    if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                        redstoneTypedSlot.checkPower();
                    }
                }
                if (this.power > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.getFrameEntity()
                            .putPanelState(direction, this.getPanelState().setValue(BlockStateProperties.POWERED, power > 0), true);
                }
            }
        }
    }

    public boolean checkValid(Object identification) {
        return this.redstoneSupplier != null && this.redstoneSupplier.identifier().equals(identification);
    }

    public int getPower() {
        return this.power;
    }
}
