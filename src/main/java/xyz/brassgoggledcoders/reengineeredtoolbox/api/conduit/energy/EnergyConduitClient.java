package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.basic.BasicIntConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import java.util.OptionalInt;
import java.util.function.Function;

public class EnergyConduitClient extends BasicIntConduitClient<EnergyContext, EnergyConduitType> {
    private EnergyConduitClient(Function<EnergyContext, OptionalInt> extractPower,
                                Function<EnergyContext, OptionalInt> insertPower,
                                FaceInstance faceInstance, ITextComponent name) {
        super(faceInstance, name, RETObjects.ENERGY_TYPE.get(), extractPower, insertPower);
    }

    public static EnergyConduitClient createSupplier(FaceInstance faceInstance, ITextComponent name,
                                                     Function<EnergyContext, OptionalInt> extractPower) {
        return new EnergyConduitClient(extractPower, power -> OptionalInt.empty(), faceInstance, name);
    }

    public static EnergyConduitClient createConsumer(FaceInstance faceInstance, ITextComponent name,
                                                     Function<EnergyContext, OptionalInt> insertPower) {
        return new EnergyConduitClient(power -> OptionalInt.empty(), insertPower, faceInstance, name);
    }
}
