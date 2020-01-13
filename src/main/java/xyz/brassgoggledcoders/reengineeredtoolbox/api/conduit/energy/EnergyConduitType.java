package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;

import java.util.OptionalInt;

public class EnergyConduitType extends ConduitType<OptionalInt, EnergyContext, EnergyConduitType> {
    public EnergyConduitType() {
        super("energy", RETObjects.ENERGY_CORE_TYPE, OptionalInt::empty);
    }
}
