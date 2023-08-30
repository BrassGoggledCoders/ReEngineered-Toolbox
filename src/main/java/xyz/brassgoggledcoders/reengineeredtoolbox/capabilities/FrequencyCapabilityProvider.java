package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.*;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item.FrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone.FrequencyRedstoneHandler;

public class FrequencyCapabilityProvider implements IFrequencyCapabilityProvider {
    private final FrequencyItemHandler frequencyItemHandler;
    private final LazyOptional<IFrequencyItemHandler> frequencyItemHandlerLazy;

    private final FrequencyRedstoneHandler frequencyRedstoneHandler;
    private final LazyOptional<IFrequencyRedstoneHandler> frequencyRedstoneHandlerLazy;

    private final FrequencyFluidHandler frequencyFluidHandler;
    private final LazyOptional<IFrequencyFluidHandler> frequencyFluidHandlerLazy;

    private final FrequencyEnergyHandler frequencyEnergyHandler;
    private final LazyOptional<IFrequencyEnergyHandler> frequencyEnergyHandlerLazy;

    public FrequencyCapabilityProvider(IFrameEntity frame) {

        this.frequencyItemHandler = new FrequencyItemHandler(frame::needsSaved);
        this.frequencyItemHandlerLazy = LazyOptional.of(() -> frequencyItemHandler);

        this.frequencyRedstoneHandler = new FrequencyRedstoneHandler(frame);
        this.frequencyRedstoneHandlerLazy = LazyOptional.of(() -> this.frequencyRedstoneHandler);

        this.frequencyFluidHandler = new FrequencyFluidHandler(frame::needsSaved);
        this.frequencyFluidHandlerLazy = LazyOptional.of(() -> this.frequencyFluidHandler);

        this.frequencyEnergyHandler = new FrequencyEnergyHandler(10000, frame::needsSaved);
        this.frequencyEnergyHandlerLazy = LazyOptional.of(() -> this.frequencyEnergyHandler);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ReEngineeredCapabilities.FREQUENCY_ITEM_HANDLER) {
            return this.frequencyItemHandlerLazy.cast();
        } else if (cap == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            return this.frequencyRedstoneHandlerLazy.cast();
        } else if (cap == ReEngineeredCapabilities.FREQUENCY_FLUID_HANDLER) {
            return this.frequencyFluidHandlerLazy.cast();
        } else if (cap == ReEngineeredCapabilities.FREQUENCY_ENERGY_HANDLER) {
            return this.frequencyEnergyHandlerLazy.cast();
        }

        return LazyOptional.empty();
    }

    public void invalidate() {
        this.frequencyItemHandlerLazy.invalidate();
        this.frequencyFluidHandlerLazy.invalidate();
        this.frequencyEnergyHandlerLazy.invalidate();
        this.frequencyRedstoneHandlerLazy.invalidate();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("FrequencyItemHandler", this.frequencyItemHandler.serializeNBT());
        compoundTag.put("FrequencyFluidHandler", this.frequencyFluidHandler.serializeNBT());
        compoundTag.put("FrequencyEnergyHandler", this.frequencyEnergyHandler.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.frequencyItemHandler.deserializeNBT(nbt.getCompound("FrequencyItemHandler"));
        this.frequencyFluidHandler.deserializeNBT(nbt.getCompound("FrequencyFluidHandler"));
        this.frequencyEnergyHandler.deserializeNBT(nbt.getCompound("FrequencyEnergyHandler"));
    }

    @Override
    public void run() {
        this.frequencyRedstoneHandler.tick();
    }
}
