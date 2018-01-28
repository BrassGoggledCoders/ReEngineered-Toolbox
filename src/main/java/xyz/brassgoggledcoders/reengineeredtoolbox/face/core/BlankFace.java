package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class BlankFace extends Face {
    public BlankFace() {
        super(new ResourceLocation(MOD_ID, "blank"));
    }
}
