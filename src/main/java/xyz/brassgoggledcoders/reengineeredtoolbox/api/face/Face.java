package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Face extends ForgeRegistryEntry<Face> {
    private final Function<Face, FaceInstance> faceInstanceConstructor;
    private final LazyLoadBase<ResourceLocation> spriteLocation;

    public Face() {
        this(FaceInstance::new);
    }

    public Face(Function<Face, FaceInstance> createFaceInstance) {
        this.faceInstanceConstructor = createFaceInstance;
        this.spriteLocation = new LazyLoadBase<>(() -> {
            ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
            return new ResourceLocation(registryName.getNamespace(), "textures/faces/" + registryName.getNamespace());
        });
    }

    public boolean isReplaceable() {
        return false;
    }

    public FaceInstance createInstance() {
        return faceInstanceConstructor.apply(this);
    }

    public ResourceLocation getSpriteLocation() {
        return spriteLocation.getValue();
    }
}
