package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class FaceItemInput extends Face {
    public FaceItemInput() {
        super(new ResourceLocation(MOD_ID, "item_input"));
    }

    @Override
    public FaceInstance createInstance() {
        return new FaceInstanceItemInput();
    }
}
