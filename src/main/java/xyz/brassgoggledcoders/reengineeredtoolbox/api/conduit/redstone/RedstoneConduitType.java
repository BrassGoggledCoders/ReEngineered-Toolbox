package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.IOType;

import java.util.OptionalInt;

public class RedstoneConduitType extends ConduitType<OptionalInt, RedstoneContext, RedstoneConduitType> {

    public RedstoneConduitType() {
        super(IOType.PULL_ONLY);
    }

    @Override
    public ConduitCore<OptionalInt, RedstoneContext, RedstoneConduitType> createCore() {
        return new RedstoneConduitCore();
    }
}
