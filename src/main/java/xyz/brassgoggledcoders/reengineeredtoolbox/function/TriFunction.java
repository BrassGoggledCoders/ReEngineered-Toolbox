package xyz.brassgoggledcoders.reengineeredtoolbox.function;

@FunctionalInterface
public interface TriFunction<T, U, V, W> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    W apply(T t, U u, V v);
}
