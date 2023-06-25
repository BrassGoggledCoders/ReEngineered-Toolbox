package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.energy;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.PanelEntityBuilder;

import java.util.function.BiFunction;

public class EnergyIOPanelEntity extends IOPanelEntity {
    private final IOStyle ioStyle;
    private final Component identifier;
    private final IEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> lazyOptional;

    public EnergyIOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                               IOStyle ioStyle, Component identifier) {
        super(type, frameEntity, panelState);
        this.ioStyle = ioStyle;
        this.identifier = identifier;
        this.energyStorage = this.createEnergyHandler();
        this.lazyOptional = LazyOptional.of(this::getEnergyStorage);
    }

    private FrequencyBackedEnergyHandler createEnergyHandler() {
        return new FrequencyBackedEnergyHandler(
                this.getIoPort(),
                this.ioStyle,
                this.getFrameEntity().getCapability(ReEngineeredCapabilities.FREQUENCY_ENERGY_HANDLER)
        );
    }

    @Override
    @NotNull
    protected Component getIdentifier() {
        return this.identifier;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.lazyOptional.invalidate();
    }

    public static BiFunction<IFrameEntity, PanelState, EnergyIOPanelEntity> energyOutput() {
        return (entity, state) -> new EnergyOutputPanelEntity(
                ReEngineeredPanels.ENERGY_OUTPUT.getPanelEntityType(),
                entity,
                state
        );
    }

    protected IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public static PanelEntityBuilder.PanelEntityFactory<EnergyIOPanelEntity> energyOutputFactory() {
        return EnergyOutputPanelEntity::new;
    }

    public static BiFunction<IFrameEntity, PanelState, EnergyIOPanelEntity> energyInput() {
        return (entity, state) -> new EnergyIOPanelEntity(
                ReEngineeredPanels.ENERGY_INPUT.getPanelEntityType(),
                entity,
                state,
                IOStyle.ONLY_INSERT,
                ReEngineeredText.ENERGY_SLOT_IN
        );
    }

    public static PanelEntityBuilder.PanelEntityFactory<EnergyIOPanelEntity> energyInputFactory() {
        return (type, entity, state) -> new EnergyIOPanelEntity(
                type,
                entity,
                state,
                IOStyle.ONLY_INSERT,
                ReEngineeredText.ENERGY_SLOT_IN
        );
    }
}
