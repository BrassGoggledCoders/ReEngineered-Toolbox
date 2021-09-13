package xyz.brassgoggledcoders.reengineeredtoolbox.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.FrameModelLoader;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void loaderRegister(ModelRegistryEvent modelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(FrameModelLoader.LOADER_ID, new FrameModelLoader());
    }
}
