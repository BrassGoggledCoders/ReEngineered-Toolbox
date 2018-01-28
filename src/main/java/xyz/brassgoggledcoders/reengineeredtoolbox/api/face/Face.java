package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.teamacronymcoders.base.util.TextureUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class Face extends IForgeRegistryEntry.Impl<Face> {
    private ResourceLocation textureLocation;
    private String unlocalizedName;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite sprite;

    public Face(ResourceLocation resourceLocation) {
        this.unlocalizedName = "face." + resourceLocation.getResourceDomain() + "." +
                resourceLocation.getResourcePath().replace("/", "_");
        this.setRegistryName(resourceLocation);
    }

    public boolean isReplaceable() {
        return true;
    }

    public ResourceLocation getTextureLocation() {
        if (textureLocation == null) {
            if (this.getRegistryName() != null) {
                textureLocation = new ResourceLocation(this.getRegistryName().getResourceDomain(), "faces/" +
                        this.getRegistryName().getResourcePath());
            } else {
                textureLocation = new ResourceLocation("reengineeredtoolbox", "empty");
            }
        }

        return textureLocation;
    }

    public ResourceLocation getModelLocation() {
        return this.getTextureLocation();
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public float getTextureOffset() {
        return 0f;
    }

    public boolean createSubItem() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getSprite() {
        if (sprite == null) {
            sprite = TextureUtils.getSprite(this.getTextureLocation());
        }
        return sprite;
    }

    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return false;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return null;
    }
}
