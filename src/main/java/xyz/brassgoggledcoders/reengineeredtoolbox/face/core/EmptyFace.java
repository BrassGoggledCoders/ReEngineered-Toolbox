package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class EmptyFace extends Face {
    public EmptyFace() {
        this.setRegistryName(MOD_ID, "empty");
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }
}
