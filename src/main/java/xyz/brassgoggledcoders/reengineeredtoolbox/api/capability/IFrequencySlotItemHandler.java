package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

@AutoRegisterCapability
public interface IFrequencySlotItemHandler {

    @NotNull
    ItemStack getStackInSlot(@NotNull Frequency frequency);

    @NotNull
    ItemStack insertItem(@NotNull Frequency frequency, @NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack extractItem(@NotNull Frequency frequency, int amount, boolean simulate);

    boolean isItemValid(@NotNull Frequency frequency, @NotNull ItemStack stack);

    void setStackInSlot(@NotNull Frequency frequency, @NotNull ItemStack stack);
}
