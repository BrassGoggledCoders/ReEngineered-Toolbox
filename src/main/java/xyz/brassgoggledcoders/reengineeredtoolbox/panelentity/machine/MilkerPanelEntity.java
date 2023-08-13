package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyBackedFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class MilkerPanelEntity extends PanelEntity {
    private final FrameSlot redstoneIn;
    private final FrameSlot energyIn;
    private final FrameSlot fluidOut;

    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;
    private final FrequencyBackedEnergyHandler energyHandler;
    private final FrequencyBackedFluidHandler fluidHandler;

    public MilkerPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this(ReEngineeredPanels.MILKER.getPanelEntityType(), frameEntity, panelState);
    }

    public MilkerPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.redstoneIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN, FrameSlotViews.TOP_RIGHT_4X4));
        this.energyIn = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ENERGY_SLOT_IN, FrameSlotViews.TOP_LEFT_4X4));
        this.fluidOut = this.registerFrameSlot(new FrameSlot(ReEngineeredText.FLUID_SLOT_OUT, FrameSlotViews.BOTTOM_CENTERED_4X4));

        this.energyHandler = new FrequencyBackedEnergyHandler(energyIn, IOStyle.ONLY_EXTRACT, frameEntity);
        this.fluidHandler = new FrequencyBackedFluidHandler(fluidOut, IOStyle.ONLY_INSERT, frameEntity);

        this.redstoneHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.redstoneHandlerLazyOptional.map(redstoneHandler -> redstoneHandler.getPower(redstoneIn.getFrequency()))
                    .ifPresent(this::setPowerAndUpdate);
        }
    }

    private void setPowerAndUpdate(int power) {
        if (power > 0 != this.getPanelState().getValue(BlockStateProperties.TRIGGERED)) {
            if (power > 0) {
                this.getFrameEntity()
                        .scheduleTick(this.getPanelPosition(), this.getPanel(), 4);
            }
            this.getFrameEntity()
                    .putPanelState(this.getPanelPosition(), this.getPanelState().setValue(BlockStateProperties.TRIGGERED, power > 0), true);
        }
    }
}
