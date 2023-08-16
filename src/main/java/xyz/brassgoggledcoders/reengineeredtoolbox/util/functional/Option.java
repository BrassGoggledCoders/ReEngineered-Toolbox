package xyz.brassgoggledcoders.reengineeredtoolbox.util.functional;

import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Option<T> {
    @NotNull None<?> EMPTY = new None<>();

    boolean exists(Predicate<T> contains);

    boolean forAll(Predicate<T> contains);

    <U> Option<U> map(Function<T, U> function);

    T orElse(T value);

    void ifPresent(Consumer<T> consumer);

    static <U> Option<U> ofLazy(LazyOptional<U> backingEnergyStorage) {
        if (backingEnergyStorage.isPresent()) {
            return new LazyOption<>(backingEnergyStorage);
        } else {
            return EMPTY.cast();
        }
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
    static <U> Option<U> fromOptional(Optional<U> optional) {
        return optional.<Option<U>>map(Some::new)
                .orElse(empty());
    }

    static <U> Option<U> ofNullable(U value) {
        if (value == null) {
            return empty();
        } else {
            return new Some<>(value);
        }
    }

    static <U> Option<U> empty() {
        return EMPTY.cast();
    }

    class None<T> implements Option<T> {

        @Override
        public boolean exists(Predicate<T> contains) {
            return false;
        }

        @Override
        public boolean forAll(Predicate<T> contains) {
            return true;
        }

        @Override
        public <U> Option<U> map(Function<T, U> function) {
            return this.cast();
        }

        @Override
        public T orElse(T value) {
            return value;
        }

        @NotNull
        @SuppressWarnings("unchecked")
        public <X> Option<X> cast() {
            return (Option<X>) this;
        }

        public void ifPresent(Consumer<T> consumer) {

        }
    }

    record Some<T>(T value) implements Option<T> {

        @Override
        public boolean exists(Predicate<T> contains) {
            return contains.test(value);
        }

        @Override
        public boolean forAll(Predicate<T> contains) {
            return contains.test(value);
        }

        @Override
        public <U> Option<U> map(Function<T, U> function) {
            U newValue = function.apply(this.value());
            return newValue != null ? new Some<>(newValue) : Option.empty();
        }

        @Override
        public T orElse(T value) {
            return this.value();
        }

        public void ifPresent(Consumer<T> consumer) {
            consumer.accept(this.value);
        }
    }

    record LazyOption<T>(LazyOptional<T> lazyValue) implements Option<T> {

        @Override
        public boolean exists(Predicate<T> contains) {
            if (lazyValue().isPresent()) {
                return this.lazyValue()
                        .filter(contains::test)
                        .isPresent();
            }

            return false;
        }

        @Override
        public boolean forAll(Predicate<T> contains) {
            if (this.lazyValue().isPresent()) {
                return this.lazyValue()
                        .filter(contains::test)
                        .isPresent();
            }
            return true;
        }

        @Override
        public <U> Option<U> map(Function<T, U> function) {
            return this.lazyValue()
                    .<Option<U>>map(value -> new Some<>(function.apply(value)))
                    .orElseGet(Option::empty);
        }

        @Override
        public T orElse(T value) {
            return this.lazyValue()
                    .orElse(value);
        }

        public void ifPresent(Consumer<T> consumer) {
            this.lazyValue().ifPresent(consumer::accept);
        }
    }
}


