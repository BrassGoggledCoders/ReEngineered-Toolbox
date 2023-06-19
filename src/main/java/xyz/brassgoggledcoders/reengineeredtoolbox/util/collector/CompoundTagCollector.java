package xyz.brassgoggledcoders.reengineeredtoolbox.util.collector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CompoundTagCollector<T, U extends Tag> implements Collector<T, CompoundTag, CompoundTag> {
    private final Function<T, String> keyFunction;
    private final Function<T, U> tagFunction;

    public CompoundTagCollector(Function<T, String> keyFunction, Function<T, U> tagFunction) {
        this.keyFunction = keyFunction;
        this.tagFunction = tagFunction;
    }

    @Override
    public Supplier<CompoundTag> supplier() {
        return CompoundTag::new;
    }

    @Override
    public BiConsumer<CompoundTag, T> accumulator() {
        return ((compoundTag, t) -> compoundTag.put(keyFunction.apply(t), tagFunction.apply(t)));
    }

    @Override
    public BinaryOperator<CompoundTag> combiner() {
        return CompoundTag::merge;
    }

    @Override
    public Function<CompoundTag, CompoundTag> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
    }

    public static <K, V, TAG extends Tag> CompoundTagCollector<Map.Entry<K, V>, TAG> forEntry(
            Function<K, String> keyFunction,
            Function<V, TAG> tagFunction
    ) {
        return new CompoundTagCollector<>(
                entry -> keyFunction.apply(entry.getKey()),
                entry -> tagFunction.apply(entry.getValue())
        );
    }
}
