package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

import java.util.function.ObjIntConsumer;

public class RedstoneInputPanelEntity extends RedstoneIOPanelEntity {
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;

    public RedstoneInputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this(ReEngineeredPanels.REDSTONE_INPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public RedstoneInputPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
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
    public void neighborChanged() {
        Direction direction = this.getFacing();
        if (direction != null) {
            int newPower = this.getLevel().getSignal(this.getBlockPos().relative(direction), direction);
            if (this.getPower() != newPower) {
                this.setPower(newPower);
                this.redstoneHandlerLazyOptional.ifPresent(IFrequencyRedstoneHandler::markRequiresUpdate);
                if (this.getPower() > 0 != this.getPanelState().getValue(BlockStateProperties.POWERED)) {
                    this.getFrameEntity()
                            .putPanelState(direction, this.getPanelState().setValue(BlockStateProperties.POWERED, this.getPower() > 0), true);
                }
            }
        }
    }

    @Override
    @NotNull
    protected Component getIdentifier() {
        return ReEngineeredText.REDSTONE_SLOT_IN;
    }
}
