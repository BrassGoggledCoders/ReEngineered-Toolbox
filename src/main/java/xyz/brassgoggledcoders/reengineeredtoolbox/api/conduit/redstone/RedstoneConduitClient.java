package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;

public class RedstoneConduitClient extends ConduitClient<OptionalInt, RedstoneContext, RedstoneConduitType> {
    private final Function<RedstoneContext, OptionalInt> getPower;

    private RedstoneConduitClient(Function<RedstoneContext, OptionalInt> getPower) {
        super(Conduits.REDSTONE.get());
        this.getPower = getPower;
    }

    public static RedstoneConduitClient createSupplier(Function<RedstoneContext, OptionalInt> getPower) {
        return new RedstoneConduitClient(getPower);
    }

    public static RedstoneConduitClient createConsumer() {
        return new RedstoneConduitClient(power -> OptionalInt.empty());
    }

    @Override
    @Nonnull
    public OptionalInt extractFrom(RedstoneContext redstoneContext) {
        return getPower.apply(redstoneContext);
    }

    @Override
    @Nonnull
    public OptionalInt insertInto(RedstoneContext redstoneContext) {
        return OptionalInt.empty();
    }

    @Override
    protected Supplier<OptionalInt> emptySupplier() {
        return OptionalInt::empty;
    }
}
