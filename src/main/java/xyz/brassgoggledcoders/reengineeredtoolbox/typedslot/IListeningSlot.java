package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IListeningSlot<T> extends ITypedSlot<T> {
    void addSupplier(String identifier, Supplier<T> supplier);

    void addConsumer(String identifier, Consumer<T> tConsumer);

    boolean containsHandler(String identifier);

    void removeHandler(String identifier);

    void checkUpdate();
}
