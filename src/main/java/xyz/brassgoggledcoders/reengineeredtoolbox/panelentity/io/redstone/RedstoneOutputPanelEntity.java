package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class RedstoneOutputPanelEntity extends RedstoneIOPanelEntity {
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;

    public RedstoneOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this(ReEngineeredPanels.REDSTONE_OUTPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneOutputPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState, ReEngineeredText.REDSTONE_SLOT_OUT);
        this.redstoneHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
    }

    public void setPowerAndUpdate(int power) {
        this.setPower(power);
        if (this.getPower() > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
            PanelState panelState = this.getPanelState().setValue(BlockStateProperties.POWERED, this.getPower() > 0);
            this.getFrameEntity()
                    .putPanelState(this.getPanelPosition(), panelState, true);
        }
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.redstoneHandlerLazyOptional.map(redstoneHandler -> redstoneHandler.getPower(this.getIoPort().getFrequency()))
                    .ifPresent(this::setPowerAndUpdate);
        }
    }

    @Override
    public int getSignal() {
        return this.getPower();
    }
}
