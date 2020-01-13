package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;

public class EmptyConduitType extends ConduitType<Empty, Empty, EmptyConduitType> {
    public EmptyConduitType() {
        super("empty", RETObjects.EMPTY_CORE_TYPE, Empty::new);
    }
}
