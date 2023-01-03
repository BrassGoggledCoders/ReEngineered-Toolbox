package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.RunnableItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.*;

public class ItemTypedSlot implements IItemTypedSlot {

    private final RunnableItemHandler itemHandler;

    public ItemTypedSlot() {
        this.itemHandler = new RunnableItemHandler(1, this::onChange);
    }

    @Override
    @NotNull
    public ItemStack getContent() {
        return this.itemHandler.getStackInSlot(0);
    }

    @Override
    public void setContent(@NotNull ItemStack content) {
        this.itemHandler.setStackInSlot(0, content);
    }

    @Override
    public boolean isEmpty() {
        return this.getContent().isEmpty();
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("itemStack", this.itemHandler.getStackInSlot(0).save(new CompoundTag()));
        return compoundTag;
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {
        this.setContent(ItemStack.of(compoundTag.getCompound("itemStack")));
    }

    @Override
    public void onReplaced(@NotNull ITypedSlotHolder typedSlotHolder, @Nullable ITypedSlot<?> replacement) {
        ItemStack current = this.itemHandler.getStackInSlot(0);
        if (replacement instanceof ItemTypedSlot itemHypperSlot) {
            current = itemHypperSlot.insert(current, false);
        }
        if (!typedSlotHolder.getHolderLevel().isClientSide()) {
            if (!current.isEmpty()) {
                ItemStack finalCurrent = current;
                current = typedSlotHolder.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .map(cap -> ItemHandlerHelper.insertItemStacked(cap, finalCurrent, false))
                        .orElse(current);
            }
            if (!current.isEmpty()) {
                BlockPos blockPos = typedSlotHolder.getPosition();
                Containers.dropItemStack(typedSlotHolder.getHolderLevel(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), current);
            }
        }
    }

    @Override
    public ItemStack insert(ItemStack inputStack, boolean simulate) {
        return this.itemHandler.insertItem(0, inputStack, simulate);
    }

    @Override
    public boolean isValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSlotLimit() {
        return 64;
    }

    @Override
    public ItemStack extract(int amount, boolean simulate) {
        return this.itemHandler.extractItem(0, amount, simulate);
    }

    public static ICapabilityProvider createForItem(ITypedSlotHolder hypper) {
        return new TypedSlotCapabilityProvider<>(
                hypper,
                ForgeCapabilities.ITEM_HANDLER,
                ItemTypedSlotItemHandler::new
        );
    }
}
