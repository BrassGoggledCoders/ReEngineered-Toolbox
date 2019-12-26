package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;

public class RedstoneConduitClient extends ConduitClient<OptionalInt, RedstoneContext, RedstoneConduitType> {
    private final Function<RedstoneContext, OptionalInt> getPower;

    private RedstoneConduitClient(ITextComponent name, Function<RedstoneContext, OptionalInt> getPower) {
        super(Conduits.REDSTONE.get(), name);
        this.getPower = getPower;
    }

    public static RedstoneConduitClient createSupplier(ITextComponent name, Function<RedstoneContext, OptionalInt> getPower) {
        return new RedstoneConduitClient(name, getPower);
    }

    public static RedstoneConduitClient createConsumer(ITextComponent name) {
        return new RedstoneConduitClient(name, power -> OptionalInt.empty());
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
