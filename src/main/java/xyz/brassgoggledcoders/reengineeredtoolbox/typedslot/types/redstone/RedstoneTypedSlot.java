package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedstoneTypedSlot implements IRedstoneTypedSlot {
    private final RedstoneSupplier EMPTY = new RedstoneSupplier(
            () -> 0,
            identifier -> true,
            "empty"
    );
    private final Map<Object, RedstoneSupplier> supplierMap;

    public RedstoneTypedSlot() {
        this.supplierMap = new HashMap<>();
    }

    @Override
    public RedstoneSupplier getContent() {
        return this.supplierMap.values()
                .stream()
                .filter(RedstoneSupplier::isValid)
                .max(RedstoneSupplier::compareTo)
                .orElse(EMPTY);
    }

    @Override
    public void setContent(@Nullable RedstoneSupplier content) {
        if (content != null) {
            this.supplierMap.put(content.identifier(), content);
        }
        this.supplierMap.entrySet().removeIf(entry -> entry.getValue().isValid());
    }

    @Override
    public CompoundTag toNBT() {
        return new CompoundTag();
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {

    }

    @Override
    public boolean containsIdentifier(Object o) {
        return this.supplierMap.containsKey(o);
    }
}
