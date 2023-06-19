package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.collector.CompoundTagCollector;

import java.util.Map;
import java.util.Optional;

public class FrequencyItemHandler implements IFrequencyItemHandler, INBTSerializable<CompoundTag> {
    private final Map<Frequency, ItemStack> itemStacks = Maps.newEnumMap(Frequency.class);
    private final Runnable onChanged;

    public FrequencyItemHandler(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(@NotNull Frequency frequency) {
        return Optional.ofNullable(itemStacks.get(frequency))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    @NotNull
    public ItemStack insertItem(@NotNull Frequency frequency, @NotNull ItemStack stack, boolean simulate) {

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!isItemValid(frequency, stack)) {
            return stack;
        } else {
            ItemStack existing = this.getStackInSlot(frequency);

            int limit = this.getStackLimit(stack);

            if (!existing.isEmpty()) {
                if (ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    limit -= existing.getCount();
                } else {
                    return stack;
                }
            }

            if (limit <= 0) {
                return stack;
            }

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate) {
                if (existing.isEmpty()) {
                    this.itemStacks.put(frequency, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                } else {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged();
            }

            return ItemStack.EMPTY;
        }

    }

    protected int getStackLimit(@NotNull ItemStack stack) {
        return Math.min(64, stack.getMaxStackSize());
    }

    @Override
    @NotNull
    public ItemStack extractItem(@NotNull Frequency frequency, int amount, boolean simulate) {

        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = this.getStackInSlot(frequency);

        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.itemStacks.put(frequency, ItemStack.EMPTY);
                onContentsChanged();
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.itemStacks.put(frequency, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged();
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public boolean isItemValid(@NotNull Frequency frequency, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void setStackInSlot(@NotNull Frequency frequency, @NotNull ItemStack stack) {
        this.itemStacks.put(frequency, stack);
        this.onContentsChanged();
    }

    private void onContentsChanged() {
        onChanged.run();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("ItemStacks", itemStacks.entrySet()
                .stream()
                .collect(CompoundTagCollector.forEntry(Frequency::name, ItemStack::serializeNBT))
        );
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag itemStacksTag = nbt.getCompound("ItemStacks");
        this.itemStacks.clear();
        for (String key : itemStacksTag.getAllKeys()) {
            Frequency.getByName(key)
                    .ifPresent(frequency -> {
                        ItemStack itemStack = ItemStack.of(itemStacksTag.getCompound(key));
                        if (!itemStack.isEmpty()) {
                            this.itemStacks.put(frequency, itemStack);
                        }
                    });
        }
    }
}
