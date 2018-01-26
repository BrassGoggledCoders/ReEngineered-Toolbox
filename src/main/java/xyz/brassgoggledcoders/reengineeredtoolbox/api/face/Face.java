package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Face extends IForgeRegistryEntry.Impl<Face> {

    public boolean isReplaceable() {
        return false;
    }

    public ResourceLocation getTextureLocations() {
        ResourceLocation textureLocation;

        if (this.getRegistryName() != null) {
            textureLocation = new ResourceLocation(this.getRegistryName().getResourceDomain(), "faces/" +
                    this.getRegistryName().getResourcePath());
        } else {
            textureLocation = new ResourceLocation("reengineeredtoolbox", "empty");
        }

        return textureLocation;
    }

    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return false;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return null;
    }
}
