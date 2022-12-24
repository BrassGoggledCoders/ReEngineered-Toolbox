package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;

public class RedstoneOutputPanelEntity extends PanelEntity {
    private int connectedSlot = -1;

    public RedstoneOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.REDSTONE_INPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneOutputPanelEntity(@NotNull PanelEntityType<RedstoneOutputPanelEntity> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    public boolean trySetSlotConnection(String identifier, int slotNumber) {
        if (identifier.equals("redstone")) {
            this.connectedSlot = slotNumber;
        }
        return true;
    }

    @Override
    public void slotUpdated(int slot) {
        if (slot == connectedSlot) {
            ITypedSlot<?> typedSlot = this.getFrameEntity()
                    .getTypedSlotHolder()
                    .getSlot(connectedSlot);

            if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                int power = redstoneTypedSlot.getContent().getAsInt();
                if (power > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.setPanelState(this.getPanelState().setValue(BlockStateProperties.POWERED, power > 0));
                }
            }
        }
    }
}
