package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public class Face extends IForgeRegistryEntry.Impl<Face> {
    @SideOnly(Side.CLIENT)
    public IBakedModel getModel() {
        return null;
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
}
