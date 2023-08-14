package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyBackedFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

import java.util.function.BiFunction;

public class FluidIOPanelEntity extends IOPanelEntity {
    private final IOStyle ioStyle;
    private final LazyOptional<IFluidHandler> lazyOptional;

    public FluidIOPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                              IOStyle ioStyle, Component identifier) {
        super(frameEntity, panelState, identifier);
        this.ioStyle = ioStyle;
        this.lazyOptional = LazyOptional.of(this::createFluidHandler);
    }

    private FrequencyBackedFluidHandler createFluidHandler() {
        return new FrequencyBackedFluidHandler(
                this.getIoPort(),
                this.getFrameEntity().getCapability(ReEngineeredCapabilities.FREQUENCY_FLUID_HANDLER),
                this.ioStyle
        );
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.lazyOptional.invalidate();
    }

    public static BiFunction<IFrameEntity, PanelState, FluidIOPanelEntity> fluidOutput() {
        return (entity, state) -> new FluidIOPanelEntity(
                entity,
                state,
                IOStyle.ONLY_EXTRACT,
                ReEngineeredText.FLUID_SLOT_OUT
        );
    }

    public static BiFunction<IFrameEntity, PanelState, FluidIOPanelEntity> fluidInput() {
        return (entity, state) -> new FluidIOPanelEntity(
                entity,
                state,
                IOStyle.ONLY_INSERT,
                ReEngineeredText.FLUID_SLOT_IN
        );
    }
}
