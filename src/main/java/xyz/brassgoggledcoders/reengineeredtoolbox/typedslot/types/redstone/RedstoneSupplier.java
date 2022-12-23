package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public record RedstoneSupplier(
        IntSupplier supplier,
        Predicate<Object> supplierValid,
        Object identifier
) implements IntSupplier, Comparable<RedstoneSupplier> {
    @Override
    public int getAsInt() {
        return (supplier != null && supplierValid().test(identifier)) ? supplier.getAsInt() : 0;
    }

    public boolean isValid() {
        return this.supplierValid().test(identifier);
    }

    @Override
    public int compareTo(@NotNull RedstoneSupplier o) {
        return Integer.compare(this.getAsInt(), o.getAsInt());
    }
}
