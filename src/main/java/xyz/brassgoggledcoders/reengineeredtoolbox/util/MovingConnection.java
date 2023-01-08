package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public class MovingConnection<T extends ITypedSlot<U>, U, V> {
    private final Class<T> tClass;
    private final ITypedSlotHolder slotHolder;
    private final Supplier<V> externalPoint;
    private final Port port;
    private final Function<T, V> convertSlot;
    private final BiPredicate<V, V> mover;
    private final ConnectionDirection direction;
    private final int maxCoolDown;
    private final int tickWait;

    private int slotId = -1;
    private int coolDown;
    private int ticks;

    public MovingConnection(Class<T> tClass, ITypedSlotHolder slotHolder, Port port, Supplier<V> externalPoint, Function<T, V> convertTo,
                            BiPredicate<V, V> mover, ConnectionDirection direction, int maxCoolDown, int tickWait) {
        this.tClass = tClass;
        this.slotHolder = slotHolder;
        this.externalPoint = externalPoint;
        this.port = port;
        this.convertSlot = convertTo;
        this.mover = mover;
        this.direction = direction;
        this.maxCoolDown = maxCoolDown;
        this.tickWait = tickWait;
    }

    public void tick() {
        if (this.tickWait >= 0) {
            this.ticks--;
            this.coolDown--;
            if (this.ticks <= 0) {
                if (this.coolDown <= 0) {
                    boolean didWork = false;
                    if (this.isConnected()) {
                        T typedSlot = this.getConnectedSlot();
                        if (typedSlot != null) {
                            boolean shouldWork = this.direction.slotReady(typedSlot);
                            if (shouldWork) {
                                didWork = this.direction.move(
                                        this.convertSlot.apply(typedSlot),
                                        this.externalPoint.get(),
                                        this.mover
                                );
                            }
                        }
                    }
                    if (!didWork) {
                        this.coolDown = this.maxCoolDown;
                    }
                }
                this.ticks = this.tickWait;
            }
        }
    }

    public void setSlotConnector(Port port, int slotId) {
        if (this.port.equals(port)) {
            if (this.matches(this.slotHolder.getSlot(slotId))) {
                this.slotId = slotId;
            }
        }
    }

    private T getConnectedSlot() {
        ITypedSlot<?> typedSlot = this.slotHolder.getSlot(slotId);
        if (this.matches(typedSlot)) {
            return tClass.cast(typedSlot);
        }
        return null;
    }

    public boolean isConnected() {
        return slotId >= 0 && this.matches(slotHolder.getSlot(slotId));
    }

    private boolean matches(ITypedSlot<?> typedSlot) {
        return tClass.isInstance(typedSlot);
    }

    public int getSlotId() {
        return slotId;
    }

    public boolean isConnectedTo(int slot) {
        return this.getSlotId() == slot;
    }

    public static MovingConnection<IItemTypedSlot, ItemStack, IItemHandler> itemConnection(
            ITypedSlotHolder holder, Port port, Supplier<IItemHandler> endpoint, ConnectionDirection direction
    ) {
        return new MovingConnection<>(
                IItemTypedSlot.class,
                holder,
                port,
                endpoint,
                itemTypedSlot -> itemTypedSlot,
                (from, to) -> {
                    boolean didWork = false;
                    for (int i = 0; i < from.getSlots(); i++) {
                        ItemStack itemStack = from.extractItem(i, 64, true);
                        if (!itemStack.isEmpty()) {
                            ItemStack notInserted = ItemHandlerHelper.insertItem(to, itemStack, true);
                            if (notInserted.getCount() != itemStack.getCount()) {
                                int inserted = itemStack.getCount() - notInserted.getCount();
                                ItemStack movedItemStack = from.extractItem(i, inserted, false);
                                didWork |= !movedItemStack.isEmpty();
                                ItemHandlerHelper.insertItem(to, movedItemStack, false);
                            }
                        }
                    }
                    return didWork;
                },
                direction,
                10,
                40
        );
    }

    public enum ConnectionDirection {
        TO_SLOT {
            @Override
            public <T> boolean move(T slot, T endpoint, BiPredicate<T, T> mover) {
                return mover.test(endpoint, slot);
            }

            @Override
            public boolean slotReady(ITypedSlot<?> typedSlot) {
                return !typedSlot.isFull();
            }
        },
        FROM_SLOT {
            @Override
            public <T> boolean move(T slot, T endpoint, BiPredicate<T, T> mover) {
                return mover.test(slot, endpoint);
            }

            @Override
            public boolean slotReady(ITypedSlot<?> typedSlot) {
                return !typedSlot.isEmpty();
            }
        };

        public abstract <T> boolean move(T slot, T endpoint, BiPredicate<T, T> mover);

        public abstract boolean slotReady(ITypedSlot<?> typedSlot);
    }
}
