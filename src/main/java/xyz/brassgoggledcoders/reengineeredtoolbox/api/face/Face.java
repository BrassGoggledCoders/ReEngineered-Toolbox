package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Face extends IForgeRegistryEntry.Impl<Face> {
    @SideOnly(Side.CLIENT)
    public IBakedModel getModel() {
        return null;
    }
}
