package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

public class RedstoneProvider<T> {
    private final WeakReference<T> reference;
    private final BiConsumer<T, ObjIntConsumer<Frequency>> providePower;
    private final Predicate<T> isValid;

    public RedstoneProvider(T provider, BiConsumer<T, ObjIntConsumer<Frequency>> providePower) {
        this.reference = new WeakReference<>(provider);
        this.providePower = providePower;
        this.isValid = value -> true;
    }

    public RedstoneProvider(T provider, Predicate<T> isValid, BiConsumer<T, ObjIntConsumer<Frequency>> providePower) {
        this.reference = new WeakReference<>(provider);
        this.providePower = providePower;
        this.isValid = isValid;
    }

    public boolean isValid() {
        T value = this.reference.get();
        if (value != null) {
            return this.isValid.test(value);
        }
        return false;
    }

    public void providePower(ObjIntConsumer<Frequency> powerConsumer) {
        T value = this.reference.get();
        if (value != null) {
            this.providePower.accept(value, powerConsumer);
        }
    }
}
