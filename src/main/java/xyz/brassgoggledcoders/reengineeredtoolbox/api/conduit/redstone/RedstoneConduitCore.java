package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class RedstoneConduitCore extends ConduitCore<OptionalInt, RedstoneContext, RedstoneConduitType> {
    public RedstoneConduitCore() {
        super(Conduits.REDSTONE.get(), new TranslationTextComponent("conduit_core.reengineeredtoolbox.redstone"));
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
