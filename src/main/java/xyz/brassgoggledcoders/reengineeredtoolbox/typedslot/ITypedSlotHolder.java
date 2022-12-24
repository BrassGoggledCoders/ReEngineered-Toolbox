package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public interface ITypedSlotHolder extends ICapabilityProvider {
    ITypedSlot<?>[] getSlots();

    ITypedSlot<?> getSlot(int slot);

    void setSlot(int slot, @Nullable ITypedSlot<?> typedSlot);

    BlockPos getPosition();

    Level getHolderLevel();

    int getHeight();

    int getWidth();
}
