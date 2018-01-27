package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class BlankFace extends Face {
    public BlankFace() {
        super("face." + MOD_ID + ".blank.name");
        this.setRegistryName(MOD_ID, "blank");
    }
}
