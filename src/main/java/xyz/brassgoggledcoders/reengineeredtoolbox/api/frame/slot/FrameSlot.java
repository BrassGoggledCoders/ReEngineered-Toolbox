package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameSlot {
    @NotNull
    private final Component name;
    @NotNull
    private Frequency frequency;

    public FrameSlot(@NotNull Component name) {
        this.name = name;
        this.frequency = Frequency.BLACK;
    }

    @NotNull
    public Component getName() {
        return this.name;
    }

    public void setFrequency(@Nullable Frequency frequency) {
        if (frequency != null) {
            this.frequency = frequency;
        }
    }

    @NotNull
    public Frequency getFrequency() {
        return this.frequency;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Frequency", this.frequency.name());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        Frequency.getByName(tag.getString("Frequency"))
                .ifPresent(this::setFrequency);
    }
}
