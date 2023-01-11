package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection;

import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.IListeningSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListeningConnection<T extends IListeningSlot<U>, U> extends Connection<T, U> {
    private final Consumer<U> consumer;
    private final Supplier<U> supplier;

    public ListeningConnection(Class<T> tClass, ITypedSlotHolder slotHolder, Port port, Consumer<U> consumer) {
        super(tClass, slotHolder, port);
        this.consumer = consumer;
        this.supplier = null;
    }

    public ListeningConnection(Class<T> tClass, ITypedSlotHolder slotHolder, Port port, Supplier<U> supplier) {
        super(tClass, slotHolder, port);
        this.supplier = supplier;
        this.consumer = null;
    }

    @Override
    protected void afterConnection(T typedSlot) {
        super.afterConnection(typedSlot);
        if (this.supplier != null) {
            typedSlot.addSupplier(this.getPort().identifier(), this.supplier);
        } else if (this.consumer != null) {
            typedSlot.addConsumer(this.getPort().identifier(), this.consumer);
        }
    }

    @Override
    public void clear() {
        T connectedSlot = this.getConnectedSlot();
        if (connectedSlot != null) {
            connectedSlot.removeHandler(this.getPort().identifier());
        }
    }

    public static ListeningConnection<IRedstoneTypedSlot, Integer> redstoneConsumer(
            ITypedSlotHolder slotHolder, Port port, Consumer<Integer> consumer
    ) {
        return new ListeningConnection<>(
                IRedstoneTypedSlot.class,
                slotHolder,
                port,
                consumer
        );
    }

    public static ListeningConnection<IRedstoneTypedSlot, Integer> redstoneSupplier(
            ITypedSlotHolder slotHolder, Port port, Supplier<Integer> supplier
    ) {
        return new ListeningConnection<>(
                IRedstoneTypedSlot.class,
                slotHolder,
                port,
                supplier
        );
    }
}
