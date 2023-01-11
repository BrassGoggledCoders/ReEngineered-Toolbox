package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RedstoneTypedSlot implements IRedstoneTypedSlot {

    private final Map<String, Consumer<Integer>> consumers;
    private final Map<String, Supplier<Integer>> suppliers;

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
    public void addSupplier(String identifier, Supplier<Integer> supplier) {
        this.suppliers.put(identifier, supplier);
    }

    @Override
    public void addConsumer(String identifier, Consumer<Integer> consumer) {
        this.consumers.put(identifier, consumer);
    }

    @Override
    public boolean containsHandler(String identifier) {
        return this.suppliers.containsKey(identifier) || this.consumers.containsKey(identifier);
    }

    @Override
    public void removeHandler(String identifier) {

    }

    @Override
    public void checkUpdate() {

    }
}
