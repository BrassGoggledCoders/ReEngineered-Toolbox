package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.IListeningSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public interface IRedstoneTypedSlot extends IListeningSlot<Integer> {

    @NotNull
    default TypedSlotType getType() {
        return TypedSlotTypes.REDSTONE.get();
    }
}
