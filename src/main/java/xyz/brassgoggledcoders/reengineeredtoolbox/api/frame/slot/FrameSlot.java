package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameSlot {
    @NotNull
    private final Component name;
    @NotNull
    private final FrameSlotView view;
    @NotNull
    private Frequency frequency;

    public FrameSlot(@NotNull Component name, FrameSlotView view) {
        this(name, view, Frequency.BLACK);
    }

    public FrameSlot(@NotNull Component name, @NotNull FrameSlotView view, @NotNull Frequency frequency) {
        this.name = name;
        this.view = view;
        this.frequency = frequency;
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

    @NotNull
    public FrameSlotView getView() {
        return view;
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
