package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.ListeningConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
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
    protected ListeningConnection<IRedstoneTypedSlot, Integer> createConnection() {
        return ListeningConnection.redstoneConsumer(
                this.getFrameEntity().getTypedSlotHolder(),
                this.getPort(),
                this::setPowerAndUpdate
        );
    }

    public void setPowerAndUpdate(int power) {
        this.setPower(power);
        if (this.getPower() > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
            PanelState panelState = this.getPanelState().setValue(BlockStateProperties.POWERED, this.getPower() > 0);
            this.getFrameEntity()
                    .putPanelState(this.getFacing(), panelState, true);
        }
    }

    @Override
    public IRedstoneTypedSlot getSlotForMenu() {
        return Optional.ofNullable(this.getConnectedSlot())
                .orElseGet(RedstoneTypedSlot::new);
    }

    @Override
    public int getSignal() {
        return Optional.ofNullable(this.getConnectedSlot())
                .map(IRedstoneTypedSlot::getContent)
                .orElse(0);
    }
}
