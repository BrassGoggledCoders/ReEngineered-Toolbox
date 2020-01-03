package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Conduits;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;

public class RedstoneConduitClient extends ConduitClient<OptionalInt, RedstoneContext, RedstoneConduitType> {
    private final Function<RedstoneContext, OptionalInt> getPower;

    private RedstoneConduitClient(FaceInstance faceInstance, ITextComponent name, Function<RedstoneContext, OptionalInt> getPower) {
        super(RETObjects.REDSTONE_TYPE.get(), faceInstance, name);
        this.getPower = getPower;
    }

    public static RedstoneConduitClient createSupplier(FaceInstance faceInstance, ITextComponent name,
                                                       Function<RedstoneContext, OptionalInt> getPower) {
        return new RedstoneConduitClient(faceInstance, name, getPower);
    }

    public static RedstoneConduitClient createConsumer(FaceInstance faceInstance, ITextComponent name) {
        return new RedstoneConduitClient(faceInstance, name, power -> OptionalInt.empty());
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
}
