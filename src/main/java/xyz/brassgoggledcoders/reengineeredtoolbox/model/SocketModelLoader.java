package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.ID;

public class SocketModelLoader implements ICustomModelLoader {
    private IUnbakedModel socketBaseModel;

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        if (modelLocation.getNamespace().equalsIgnoreCase(ID)) {
            return modelLocation.getPath().replace("models/", "").equalsIgnoreCase("socket");
        }
        return false;
    }

    @Override
    public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
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
