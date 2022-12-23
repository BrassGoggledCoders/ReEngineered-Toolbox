package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public interface IRedstoneTypedSlot extends ITypedSlot<RedstoneSupplier> {

    boolean containsIdentifier(Object o);

    @NotNull
    default TypedSlotType getType() {
        return TypedSlotTypes.REDSTONE.get();
    }
}
