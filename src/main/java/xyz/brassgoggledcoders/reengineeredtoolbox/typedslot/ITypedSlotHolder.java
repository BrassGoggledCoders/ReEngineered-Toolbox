package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public interface ITypedSlotHolder extends ICapabilityProvider, INBTSerializable<CompoundTag> {
    ITypedSlot<?>[] getSlots();

    ITypedSlot<?> getSlot(int slot);

    void setSlot(int slot, @Nullable ITypedSlot<?> typedSlot);

    BlockPos getPosition();

    Level getHolderLevel();

    int getHeight();

    int getWidth();

    TypedSlotHolderState getState();

    boolean matches(TypedSlotHolderState slotHolderState);

    int getSize();
}
