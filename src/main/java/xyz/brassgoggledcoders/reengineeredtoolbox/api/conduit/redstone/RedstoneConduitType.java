package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;

import java.util.OptionalInt;

public class RedstoneConduitType extends ConduitType<OptionalInt, RedstoneContext, RedstoneConduitType> {
    public RedstoneConduitType() {
        super(Conduits.REDSTONE_CORE_TYPE, OptionalInt::empty);
    }
}
