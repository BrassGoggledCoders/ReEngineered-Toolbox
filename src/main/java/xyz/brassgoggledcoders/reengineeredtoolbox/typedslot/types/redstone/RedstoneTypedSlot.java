package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RedstoneTypedSlot implements IRedstoneTypedSlot {

    private final Map<Port, Consumer<Integer>> consumers;
    private final Map<Port, Supplier<Integer>> suppliers;

    private Runnable onChange = null;
    private int lastPower = -1;

    public RedstoneTypedSlot() {
        this.consumers = new HashMap<>();
        this.suppliers = new HashMap<>();
    }

    @Override
    @NotNull
    public Integer getContent() {
        return lastPower;
    }

    @Override
    public void setContent(@NotNull Integer content) {
        this.lastPower = content;
    }

    @Override
    public void setOnChange(Runnable runnable) {
        this.onChange = runnable;
    }

    @Override
    public void onChange() {
        if (this.onChange != null) {
            this.onChange.run();
        }
    }

    @Override
    public CompoundTag toNBT() {
        return new CompoundTag();
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {

    }

    @Override
    public void addSupplier(Port port, Supplier<Integer> supplier) {
        this.suppliers.put(port, supplier);
    }

    @Override
    public void addConsumer(Port port, Consumer<Integer> consumer) {
        this.consumers.put(port, consumer);
    }

    @Override
    public boolean containsHandler(Port port) {
        return this.suppliers.containsKey(port) || this.consumers.containsKey(port);
    }

    @Override
    public void removeHandler(Port port) {
        this.suppliers.remove(port);
        this.consumers.remove(port);
    }

    @Override
    public void checkUpdate() {

    }
}
