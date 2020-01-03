package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.basic;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.function.Function;

public abstract class BasicIntConduitClient<CONTEXT, TYPE extends ConduitType<OptionalInt, CONTEXT, TYPE>>
        extends ConduitClient<OptionalInt, CONTEXT, TYPE> {

    private final Function<CONTEXT, OptionalInt> extractPower;
    private final Function<CONTEXT, OptionalInt> insertPower;

    protected BasicIntConduitClient(FaceInstance faceInstance, ITextComponent name, TYPE conduitType,
                                    Function<CONTEXT, OptionalInt> extractPower,
                                    Function<CONTEXT, OptionalInt> insertPower) {
        super(conduitType, faceInstance, name);
        this.extractPower = extractPower;
        this.insertPower = insertPower;
    }

    @Override
    @Nonnull
    public OptionalInt extractFrom(CONTEXT context) {
        return extractPower.apply(context);
    }

    @Override
    @Nonnull
    public OptionalInt insertInto(CONTEXT context) {
        return insertPower.apply(context);
    }
}
