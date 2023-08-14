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
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;

import java.util.function.BiFunction;

public class EnergyIOPanelEntity extends IOPanelEntity {
    private final IOStyle ioStyle;
    private final IEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> lazyOptional;

    public EnergyIOPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                               IOStyle ioStyle, Component identifier) {
        super(frameEntity, panelState, identifier);
        this.ioStyle = ioStyle;
        this.energyStorage = this.createEnergyHandler();
        this.lazyOptional = LazyOptional.of(this::getEnergyStorage);
    }

    private FrequencyBackedEnergyHandler createEnergyHandler() {
        return new FrequencyBackedEnergyHandler(
                this.getIoPort(),
                this.getFrameEntity().getCapability(ReEngineeredCapabilities.FREQUENCY_ENERGY_HANDLER),
                this.ioStyle
        );
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
        return EnergyOutputPanelEntity::new;
    }

    protected IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public static BiFunction<IFrameEntity, PanelState, EnergyIOPanelEntity> energyInput() {
        return (entity, state) -> new EnergyIOPanelEntity(
                entity,
                state,
                IOStyle.ONLY_INSERT,
                ReEngineeredText.ENERGY_SLOT_IN
        );
    }
}
