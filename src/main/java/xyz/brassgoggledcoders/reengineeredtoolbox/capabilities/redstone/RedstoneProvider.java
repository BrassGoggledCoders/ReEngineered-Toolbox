package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

public class RedstoneProvider<T> {
    private final WeakReference<T> reference;
    private final BiConsumer<T, ObjIntConsumer<Frequency>> providePower;

    public RedstoneProvider(T provider, BiConsumer<T, ObjIntConsumer<Frequency>> providePower) {
        this.reference = new WeakReference<>(provider);
        this.providePower = providePower;
    }

    public boolean isValid() {
        return this.reference.get() != null;
    }

    public void providePower(ObjIntConsumer<Frequency> powerConsumer) {
        T value = this.reference.get();
        if (value != null) {
            this.providePower.accept(value, powerConsumer);
        }
    }
}
