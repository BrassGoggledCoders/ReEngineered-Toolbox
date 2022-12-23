package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.ItemTypedSlot;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TypedSlotHolder implements ITypedSlotHolder {
    private final Supplier<Level> level;
    private final BlockPos blockPos;

    private final Map<TypedSlotType, ICapabilityProvider> capabilityProviderMap;

    private final int width;
    private final int height;

    private final ITypedSlot<?>[] typedSlots;
    private final Runnable onChange;

    public TypedSlotHolder(Supplier<Level> level, BlockPos blockPos, Runnable onChange) {
        this(level, blockPos, 3, 3, onChange);
    }

    public TypedSlotHolder(Supplier<Level> level, BlockPos blockPos, int width, int height, Runnable onChange) {
        this.level = level;
        this.blockPos = blockPos;
        this.onChange = onChange;
        this.typedSlots = new ITypedSlot<?>[width * height];
        this.capabilityProviderMap = new IdentityHashMap<>();
        this.width = width;
        this.height = height;
    }

    @Override
    public ITypedSlot<?>[] getSlots() {
        return typedSlots;
    }

    @Override
    public void setSlot(int slot, @Nullable ITypedSlot<?> typedSlot) {
        if (typedSlot == null) {
            typedSlot = new ItemTypedSlot();
        }
        if (slot < this.getSlots().length) {
            ITypedSlot<?> existingTypedSlot = this.getSlots()[slot];
            existingTypedSlot.onReplaced(this, typedSlot);
            this.getSlots()[slot] = typedSlot;
            this.onChange.run();
            if (!this.capabilityProviderMap.containsKey(typedSlot.getType())) {
                this.capabilityProviderMap.put(typedSlot.getType(), typedSlot.getType().createProvider(this));
            }
        }
    }

    @Override
    public BlockPos getPosition() {
        return blockPos;
    }

    @Override
    public Level getHolderLevel() {
        return this.level.get();
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        for (ICapabilityProvider provider : this.capabilityProviderMap.values()) {
            LazyOptional<T> lazyOptional = provider.getCapability(cap, side);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return LazyOptional.empty();
    }
}
