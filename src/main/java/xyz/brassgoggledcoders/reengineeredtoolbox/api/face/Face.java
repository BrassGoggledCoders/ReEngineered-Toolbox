package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class Face extends ForgeRegistryEntry<Face> {
    private Supplier<FaceInstance> faceInstanceConstructor;

    public Face() {
        this(FaceInstance::new);
    }

    public Face(Supplier<FaceInstance> createFaceInstance) {
        this.faceInstanceConstructor = createFaceInstance;
    }

    public boolean isReplaceable() {
        return true;
    }

    public FaceInstance createInstance() {
        return faceInstanceConstructor.get();
    }
}
