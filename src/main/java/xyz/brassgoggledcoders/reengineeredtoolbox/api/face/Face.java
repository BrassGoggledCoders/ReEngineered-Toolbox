package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Lists;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Face extends ForgeRegistryEntry<Face> {
    private final Function<SocketContext, FaceInstance> faceInstanceConstructor;
    private ResourceLocation defaultSpriteLocation;
    private List<ResourceLocation> allSpriteLocations;
    private String translationKey;
    private ITextComponent name;

    public Face() {
        this(FaceInstance::new);
    }

    public Face(Function<SocketContext, FaceInstance> faceInstanceConstructor) {
        this(faceInstanceConstructor, null, null);
    }

    public Face(Function<SocketContext, FaceInstance> faceInstanceConstructor, ResourceLocation defaultSpriteLocation) {
        this(faceInstanceConstructor, defaultSpriteLocation, Optional.ofNullable(defaultSpriteLocation)
                .map(Lists::newArrayList)
                .orElse(null));
    }

    public Face(Function<SocketContext, FaceInstance> faceInstanceConstructor, ResourceLocation defaultSpriteLocation,
                List<ResourceLocation> allSpriteLocations) {
        this.faceInstanceConstructor = faceInstanceConstructor;
        this.defaultSpriteLocation = defaultSpriteLocation;
        this.allSpriteLocations = allSpriteLocations;
    }

    public FaceInstance createInstance(SocketContext context) {
        return faceInstanceConstructor.apply(context);
    }

    public ITextComponent getName() {
        if (this.name == null) {
            this.name = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.name;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("face", this.getRegistryName());
        }
        return this.translationKey;
    }

    public ResourceLocation getDefaultSpriteLocation() {
        if (this.defaultSpriteLocation == null) {
            this.defaultSpriteLocation = Optional.ofNullable(this.getRegistryName())
                    .map(this::createDefaultSpriteLocation)
                    .orElseGet(() -> new ResourceLocation("reengineeredtoolbox", "faces/blank"));
        }
        return this.defaultSpriteLocation;
    }

    public List<ResourceLocation> getAllSpriteLocations() {
        if (this.allSpriteLocations == null) {
            this.allSpriteLocations = Lists.newArrayList(this.getDefaultSpriteLocation());
        }
        return this.allSpriteLocations;
    }

    public boolean isValidForSide(Direction side) {
        return true;
    }

    private ResourceLocation createDefaultSpriteLocation(ResourceLocation registryName) {
        return new ResourceLocation(registryName.getNamespace(), "faces/" + registryName.getPath());
    }
}
