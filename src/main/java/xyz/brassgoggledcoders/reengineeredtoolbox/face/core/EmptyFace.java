package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class EmptyFace extends Face {
    public EmptyFace() {
        super(new ResourceLocation(MOD_ID, "empty"));
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public float getTextureOffset() {
        return 0.125f;
    }

    @Override
    public boolean createSubItem() {
        return false;
    }
}
