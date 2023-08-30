package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;

public class RedstoneTrigger implements INBTSerializable<CompoundTag> {
    private final FrameSlot frameSlot;
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;
    private final BooleanConsumer onPowerStatusChanged;

    private int power;

    public RedstoneTrigger(FrameSlot frameSlot, LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional, BooleanConsumer onPowerChanged) {
        this.frameSlot = frameSlot;
        this.redstoneHandlerLazyOptional = redstoneHandlerLazyOptional;
        this.onPowerStatusChanged = onPowerChanged;
    }

    public void onFrameSlotChange(FrameSlot updated) {
        if (updated == frameSlot) {
            this.checkPower();
        }
    }

    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.checkPower();
        }
    }

    private void checkPower() {
        this.redstoneHandlerLazyOptional.ifPresent(this::checkPower);
    }

    private void checkPower(IFrequencyRedstoneHandler handler) {
        int newPower = handler.getPower(this.frameSlot.getFrequency());
        if (newPower != power) {
            if (newPower > 0 != power > 0) {
                this.onPowerStatusChanged.accept(newPower > 0);
            }
            this.power = newPower;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("FrameSlot", this.frameSlot.serializeNBT());
        compoundTag.putInt("Power", this.power);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.frameSlot.deserializeNBT(nbt.getCompound("FrameSlot"));
        this.power = nbt.getInt("Power");
    }
}
