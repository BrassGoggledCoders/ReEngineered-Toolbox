package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SingleSlotTypedSlotHolder<U> implements ITypedSlotHolder {
    private final Supplier<ITypedSlot<U>> typedSlot;

    public SingleSlotTypedSlotHolder(Supplier<ITypedSlot<U>> typedSlot) {
        this.typedSlot = typedSlot;
    }

    @Override
    public ITypedSlot<?>[] getSlots() {
        return new ITypedSlot[]{typedSlot.get()};
    }

    @Override
    public ITypedSlot<?> getSlot(int slot) {
        return typedSlot.get();
    }

    @Override
    public void setSlot(int slot, @Nullable ITypedSlot<?> typedSlot) {

    }

    @Override
    public BlockPos getPosition() {
        return null;
    }

    @Override
    public Level getHolderLevel() {
        return null;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public TypedSlotHolderState getState() {
        return new TypedSlotHolderState(
                this.getHeight(),
                this.getWidth(),
                new TypedSlotState[]{
                        this.typedSlot.get().getState()
                }
        );
    }

    @Override
    public boolean matches(TypedSlotHolderState slotHolderState) {
        return this.typedSlot.get().matches(slotHolderState.slotStates()[0]);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
