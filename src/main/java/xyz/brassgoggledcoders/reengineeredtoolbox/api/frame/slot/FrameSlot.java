package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TextComponentTagVisitor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.undo.CompoundEdit;

public class FrameSlot {
    @NotNull
    private final Component name;
    @NotNull
    private ChatFormatting frequency;

    public FrameSlot(@NotNull Component name) {
        this.name = name;
        this.frequency = ChatFormatting.BLACK;
    }

    @NotNull
    public Component getName() {
        return this.name;
    }

    public void setFrequency(@Nullable ChatFormatting frequency) {
        if (frequency != null && frequency.isColor()) {
            this.frequency = frequency;
        }
    }

    @NotNull
    public ChatFormatting getFrequency() {
        return this.frequency;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Frequency", this.frequency.getId());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.setFrequency(ChatFormatting.getById(tag.getInt("Frequency")));
    }
}
