package xyz.brassgoggledcoders.reengineeredtoolbox.face.io;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class FaceItemOutput extends Face {
    public FaceItemOutput() {
        super(new ResourceLocation(MOD_ID, "item_output"));
    }
}
