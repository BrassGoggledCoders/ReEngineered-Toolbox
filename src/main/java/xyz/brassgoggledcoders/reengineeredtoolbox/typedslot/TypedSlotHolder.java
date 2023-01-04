package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class TypedSlotHolder implements ITypedSlotHolder, INBTSerializable<CompoundTag> {
    private final Supplier<Level> level;
    private final BlockPos blockPos;

    private final Map<TypedSlotType, ICapabilityProvider> capabilityProviderMap;

    private final int width;
    private final int height;

    private final ITypedSlot<?>[] typedSlots;
    private final IntConsumer onChange;

    public TypedSlotHolder(Supplier<Level> level, BlockPos blockPos, IntConsumer onChange) {
        this(level, blockPos, 3, 3, onChange);
    }

    public TypedSlotHolder(Supplier<Level> level, BlockPos blockPos, int width, int height, IntConsumer onChange) {
        this.level = level;
        this.blockPos = blockPos;
        this.onChange = onChange;
        this.typedSlots = new ITypedSlot<?>[width * height];
        for (int i = 0; i < this.typedSlots.length; i++) {
            this.typedSlots[i] = TypedSlotTypes.BLANK.get().createSlot();
        }
        this.capabilityProviderMap = new IdentityHashMap<>();
        this.width = width;
        this.height = height;
    }

    @Override
    public ITypedSlot<?>[] getSlots() {
        return typedSlots;
    }

    @Override
    public ITypedSlot<?> getSlot(int slot) {
        if (slot >= 0 && slot < this.getSlots().length) {
            return this.getSlots()[slot];
        }
        return null;
    }

    @Override
    public void setSlot(int slot, @Nullable ITypedSlot<?> typedSlot) {
        if (typedSlot == null) {
            typedSlot = TypedSlotTypes.BLANK.get().createSlot();
        }
        if (slot < this.getSlots().length) {
            ITypedSlot<?> existingTypedSlot = this.getSlots()[slot];
            existingTypedSlot.onReplaced(this, typedSlot);
            typedSlot.setOnChange(() -> this.onChange.accept(slot));
            this.getSlots()[slot] = typedSlot;
            this.onChange.accept(slot);

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
    public TypedSlotHolderState getState() {
        return new TypedSlotHolderState(
                this.getHeight(),
                this.getWidth(),
                this.getSlotStates()
        );
    }

    @Override
    public boolean matches(TypedSlotHolderState slotHolderState) {
        boolean sizeMatches = slotHolderState.height() == this.getHeight() && slotHolderState.width() == this.getWidth();
        if (sizeMatches) {
            for (int i = 0; i < this.getSlots().length; i++) {
                if (!this.getSlot(i).matches(slotHolderState.slotStates()[i])) {
                    return false;
                }
            }
        }
        return sizeMatches;
    }

    @Override
    public int getSize() {
        return this.getSlots().length;
    }

    public TypedSlotState[] getSlotStates() {
        TypedSlotState[] typedSlotStates = new TypedSlotState[this.getSlots().length];
        for (int i = 0; i < this.getSlots().length; i++) {
            ITypedSlot<?> typedSlot = this.getSlot(i);
            typedSlotStates[i] = new TypedSlotState(
                    typedSlot.getType(),
                    typedSlot.isEmpty()
            );
        }
        return typedSlotStates;
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        CompoundTag slotsTag = new CompoundTag();
        for (int x = 0; x < this.getSize(); x++) {
            ITypedSlot<?> typedSlot = this.getSlot(x);
            ResourceLocation id = TypedSlotTypes.getRegistry().getKey(typedSlot.getType());
            if (id != null) {
                CompoundTag slotTag = typedSlot.toNBT();
                slotTag.putString("Id", id.toString());
                slotsTag.put(Integer.toString(x), slotTag);
            }
        }
        compoundTag.put("Slots", slotsTag);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag slotsTag = nbt.getCompound("Slots");
        for (int x = 0; x < this.getSize(); x++) {
            CompoundTag slotTag = slotsTag.getCompound(Integer.toString(x));
            ITypedSlot<?> newSlot = null;
            if (!slotsTag.isEmpty()) {
                TypedSlotType type = TypedSlotTypes.getRegistry()
                        .getValue(new ResourceLocation(slotTag.getString("Id")));

                if (type != null) {
                    newSlot = type.createSlot();
                    newSlot.fromNBT(slotTag);
                }
            }

            if (newSlot == null) {
                newSlot = TypedSlotTypes.BLANK.get().createSlot();
            }
            this.typedSlots[x] = newSlot;
        }
    }
}
