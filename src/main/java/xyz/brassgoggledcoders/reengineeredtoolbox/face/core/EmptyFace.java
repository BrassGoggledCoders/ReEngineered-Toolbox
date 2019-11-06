package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class EmptyFace extends Face {
    public EmptyFace() {
        super();
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }
}
