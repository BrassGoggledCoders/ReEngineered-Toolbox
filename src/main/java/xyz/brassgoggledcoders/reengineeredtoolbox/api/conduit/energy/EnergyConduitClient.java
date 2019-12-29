package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.basic.BasicIntConduitClient;

import java.util.OptionalInt;
import java.util.function.Function;

public class EnergyConduitClient extends BasicIntConduitClient<EnergyContext, EnergyConduitType> {
    private EnergyConduitClient(Function<EnergyContext, OptionalInt> extractPower,
                                Function<EnergyContext, OptionalInt> insertPower, ITextComponent name) {
        super(extractPower, insertPower, RETObjects.ENERGY_TYPE.get(), name);
    }

    public static EnergyConduitClient createSupplier(Function<EnergyContext, OptionalInt> extractPower, ITextComponent name) {
        return new EnergyConduitClient(extractPower, power -> OptionalInt.empty(), name);
    }

    public static EnergyConduitClient createConsumer(Function<EnergyContext, OptionalInt> insertPower, ITextComponent name) {
        return new EnergyConduitClient(power -> OptionalInt.empty(), insertPower, name);
    }
}
