package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Lists;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Face extends ForgeRegistryEntry<Face> {
    private final Function<SocketContext, FaceInstance> faceInstanceConstructor;
    private final LazyLoadBase<ResourceLocation> spriteLocation;
    private final LazyLoadBase<String> translationKey;
    private final LazyLoadBase<ITextComponent> name;

    public Face() {
        this(FaceInstance::new);
    }

    public Face(Function<SocketContext, FaceInstance> createFaceInstance) {
        this.faceInstanceConstructor = createFaceInstance;
        this.spriteLocation = new LazyLoadBase<>(() -> {
            ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
            return new ResourceLocation(registryName.getNamespace(), "faces/" + registryName.getPath());
        });
        this.translationKey = new LazyLoadBase<>(() -> {
            ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
            return "face." + registryName.getNamespace() + "." + registryName.getPath();
        });
        this.name = new LazyLoadBase<>(() -> new TranslationTextComponent(this.translationKey.getValue()));
    }

    public FaceInstance createInstance(SocketContext context) {
        return faceInstanceConstructor.apply(context);
    }

    public ITextComponent getName() {
        return this.name.getValue();
    }

    public String getTranslationKey() {
        return this.translationKey.getValue();
    }

    public ResourceLocation getSpriteLocation() {
        return spriteLocation.getValue();
    }

    public List<ResourceLocation> getAllSprites() {
        return Lists.newArrayList(spriteLocation.getValue());
    }

    public boolean isValidForSide(Direction side) {
        return true;
    }
}
