package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IListeningSlot<T> extends ITypedSlot<T> {
    void addSupplier(Port port, Supplier<T> supplier);

    void addConsumer(Port port, Consumer<T> tConsumer);


    void removeHandler(Port port);

    boolean checkUpdate();
}
