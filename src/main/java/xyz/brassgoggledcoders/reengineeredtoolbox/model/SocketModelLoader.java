package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.ID;

@SideOnly(Side.CLIENT)
public class SocketModelLoader implements ICustomModelLoader {
    private IModel socketBaseModel;

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equalsIgnoreCase(ID) &&
                modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("socket");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        if (socketBaseModel == null) {
            socketBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation(ID, "block/frame"));
        }
        return new SocketModel(socketBaseModel);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        socketBaseModel = null;
    }
}
