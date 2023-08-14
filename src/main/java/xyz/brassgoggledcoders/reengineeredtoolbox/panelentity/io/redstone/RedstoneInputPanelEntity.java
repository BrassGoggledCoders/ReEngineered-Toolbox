package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

import java.util.function.ObjIntConsumer;

public class RedstoneInputPanelEntity extends RedstoneIOPanelEntity {
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;

    public RedstoneInputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState, ReEngineeredText.REDSTONE_SLOT_IN);
        this.redstoneHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
        this.redstoneHandlerLazyOptional.ifPresent(redstoneHandler -> redstoneHandler.addPowerProvider(
                this,
                RedstoneInputPanelEntity::providePower
        ));
    }

    public void providePower(ObjIntConsumer<Frequency> powerSubmit) {
        powerSubmit.accept(this.getIoPort().getFrequency(), this.getPower());
    }

    @Override
    public void onRemove() {
        super.onRemove();
        this.redstoneHandlerLazyOptional.ifPresent(IFrequencyRedstoneHandler::markRequiresUpdate);
    }

    @Override
    public void neighborChanged() {
        IPanelPosition panelPosition = this.getPanelPosition();
        Direction direction = panelPosition.getFacing();
        if (direction != null) {
            int newPower = this.getLevel().getSignal(panelPosition.offset(this.getFrameEntity()), direction);
            if (this.getPower() != newPower) {
                this.setPower(newPower);
                this.redstoneHandlerLazyOptional.ifPresent(IFrequencyRedstoneHandler::markRequiresUpdate);
                if (this.getPower() > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.getFrameEntity()
                            .putPanelState(
                                    panelPosition,
                                    this.getPanelState()
                                            .setValue(BlockStateProperties.POWERED, this.getPower() > 0), true
                            );
                }
            }
        }
    }
}
