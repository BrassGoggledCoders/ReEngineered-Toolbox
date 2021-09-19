package xyz.brassgoggledcoders.reengineeredtoolbox.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredToolboxAPI;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panel.DefaultPanelModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panel.PanelModelRegistry;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void createPanelModelRegistry(ModelRegistryEvent modelRegistryEvent) {
        ReEngineeredToolboxAPI.setPanelModelRegistry(PanelModelRegistry.INSTANCE);
    }

    @SubscribeEvent
    public static void loaderRegister(ModelRegistryEvent modelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(FrameModelLoader.LOADER_ID, new FrameModelLoader());
        ReEngineeredToolboxAPI.getPanelModelRegistry()
                .registerModelLoader(DefaultPanelModelLoader.ID, new DefaultPanelModelLoader());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        PanelModelRegistry.INSTANCE.freezeRegistry();
    }
}
