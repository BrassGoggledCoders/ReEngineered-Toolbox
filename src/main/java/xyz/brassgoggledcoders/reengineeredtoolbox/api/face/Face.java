package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Lists;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Face extends ForgeRegistryEntry<Face> {
    private final Function<Face, FaceInstance> faceInstanceConstructor;
    private final LazyLoadBase<ResourceLocation> spriteLocation;
    private final LazyLoadBase<ITextComponent> textComponent;

    public Face() {
        this(FaceInstance::new);
    }

    public Face(Function<Face, FaceInstance> createFaceInstance) {
        this.faceInstanceConstructor = createFaceInstance;
        this.spriteLocation = new LazyLoadBase<>(() -> {
            ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
            return new ResourceLocation(registryName.getNamespace(), "faces/" + registryName.getPath());
        });
        this.textComponent = new LazyLoadBase<>(() -> {
            ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
            return new TranslationTextComponent("face." + registryName.getNamespace() + "." + registryName.getPath());
        });
    }

    public FaceInstance createInstance() {
        return faceInstanceConstructor.apply(this);
    }

    public ITextComponent getName() {
        return this.textComponent.getValue();
    }

    public ResourceLocation getSpriteLocation() {
        return spriteLocation.getValue();
    }

    public List<ResourceLocation> getAllSprites() {
        return Lists.newArrayList(spriteLocation.getValue());
    }
}
