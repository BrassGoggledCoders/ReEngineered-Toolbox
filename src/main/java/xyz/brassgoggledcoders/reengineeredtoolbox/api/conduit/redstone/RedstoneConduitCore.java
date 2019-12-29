package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class RedstoneConduitCore extends ConduitCore<OptionalInt, RedstoneContext, RedstoneConduitType> {
    public RedstoneConduitCore() {
        super(RETObjects.REDSTONE_TYPE.get(), RETObjects.REDSTONE_CORE_TYPE.get());
    }

    @Override
    @Nonnull
    public OptionalInt request(RedstoneContext redstoneContext) {
        return this.getClients().stream()
                .map(client -> client.extractFrom(redstoneContext))
                .flatMapToInt(power -> IntStream.of(power.orElse(0)))
                .max();
    }

    @Override
    @Nonnull
    public OptionalInt offer(RedstoneContext context) {
        return OptionalInt.empty();
    }
}
