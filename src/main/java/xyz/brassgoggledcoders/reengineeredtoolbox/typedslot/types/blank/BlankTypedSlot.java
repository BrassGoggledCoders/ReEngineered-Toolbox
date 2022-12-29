package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.blank;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public class BlankTypedSlot implements ITypedSlot<Void> {
    @Override
    @NotNull
    public TypedSlotType getType() {
        return TypedSlotTypes.BLANK.get();
    }

    @Override
    public Void getContent() {
        return null;
    }

    @Override
    public void setContent(Void content) {

    }

    @Override
    public CompoundTag toNBT() {
        return new CompoundTag();
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {

    }
}
