package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.*;

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
