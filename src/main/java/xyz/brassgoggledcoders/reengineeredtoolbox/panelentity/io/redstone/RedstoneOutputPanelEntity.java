package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneTypedSlot;

import java.util.Optional;

public class RedstoneOutputPanelEntity extends RedstoneIOPanelEntity {

    public RedstoneOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.REDSTONE_OUTPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneOutputPanelEntity(@NotNull PanelEntityType<RedstoneOutputPanelEntity> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    public void slotUpdated(int slot) {
        if (slot == this.getConnectedSlotId()) {
            ITypedSlot<?> typedSlot = this.getFrameEntity()
                    .getTypedSlotHolder()
                    .getSlot(this.getConnectedSlotId());

            if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                int newPower = redstoneTypedSlot.getContent().getAsInt();
                this.setPower(newPower);
                if (newPower > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.getFrameEntity()
                            .putPanelState(this.getFacing(), this.getPanelState().setValue(BlockStateProperties.POWERED, this.getPower() > 0), true);

                }
            }
        }
    }

    @Override
    public IRedstoneTypedSlot getSlotForMenu() {
        return Optional.ofNullable(this.getConnectedSlot())
                .orElseGet(RedstoneTypedSlot::new);
    }
}
