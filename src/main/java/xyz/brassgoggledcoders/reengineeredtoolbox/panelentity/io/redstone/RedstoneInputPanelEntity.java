package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneTypedSlot;

public class RedstoneInputPanelEntity extends RedstoneIOPanelEntity {

    public RedstoneInputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.REDSTONE_INPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneInputPanelEntity(@NotNull PanelEntityType<RedstoneInputPanelEntity> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    protected void afterConnection(IRedstoneTypedSlot typedSlot) {
        typedSlot.addSupplier(this.getIdentifier(), this::getPower);
    }

    @Override
    public IRedstoneTypedSlot getSlotForMenu() {
        IRedstoneTypedSlot redstoneTypedSlot = new RedstoneTypedSlot();
        redstoneTypedSlot.addSupplier(this.getIdentifier(), this::getPower);
        return redstoneTypedSlot;
    }

    @Override
    public void neighborChanged() {
        Direction direction = this.getFacing();
        if (direction != null) {
            int newPower = this.getLevel().getSignal(this.getBlockPos().relative(direction), direction);
            if (this.getPower() != newPower) {
                this.setPower(newPower);
                if (this.getConnectedSlotId() >= 0) {
                    ITypedSlot<?> typedSlot = this.getFrameEntity()
                            .getTypedSlotHolder()
                            .getSlot(this.getConnectedSlotId());

                    if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
                        redstoneTypedSlot.checkUpdate();
                    }
                }
                if (this.getPower() > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.getFrameEntity()
                            .putPanelState(direction, this.getPanelState().setValue(BlockStateProperties.POWERED, this.getPower() > 0), true);
                }
            }
        }
    }
}
