package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class RedstoneOutputPanelEntity extends RedstoneIOPanelEntity {

    public RedstoneOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.REDSTONE_OUTPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneOutputPanelEntity(@NotNull PanelEntityType<RedstoneOutputPanelEntity> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
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
    public int getSignal() {
        return 0;//Optional.ofNullable(this.getConnectedSlot())
        //.map(IRedstoneTypedSlot::getContent)
        //.orElse(0);
    }

    @Override
    @NotNull
    protected Component getIdentifier() {
        return ReEngineeredText.REDSTONE_SLOT_OUT;
    }
}
