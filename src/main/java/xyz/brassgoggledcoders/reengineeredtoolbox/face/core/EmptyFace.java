package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.ID;

public class EmptyFace extends Face {
    public EmptyFace() {
        super(new ResourceLocation(ID, "empty"));
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
