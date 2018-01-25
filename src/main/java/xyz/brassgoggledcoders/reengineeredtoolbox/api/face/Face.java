package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Face extends IForgeRegistryEntry.Impl<Face> {
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> getModel() {
        return Collections.emptyList();
    }

    public boolean isReplaceable() {
        return true;
    }

    public List<ResourceLocation> getTextureLocations() {
        List<ResourceLocation> textureLocations = Lists.newArrayList();

        if (this.getRegistryName() != null) {
            textureLocations.add(new ResourceLocation(this.getRegistryName().getResourceDomain(), "faces/" +
                    this.getRegistryName().getResourcePath()));
        }

        return textureLocations;
    }

    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return false;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return null;
    }
}
