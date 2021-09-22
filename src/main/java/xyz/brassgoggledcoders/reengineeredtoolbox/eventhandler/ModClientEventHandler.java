package xyz.brassgoggledcoders.reengineeredtoolbox.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panel.PanelModelManager;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void loaderRegister(ModelRegistryEvent modelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(FrameModelLoader.LOADER_ID, new FrameModelLoader());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ((IReloadableResourceManager) Minecraft.getInstance()
                .getResourceManager())
                .registerReloadListener(PanelModelManager.INSTANCE)
        );
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        PanelModelManager.INSTANCE.getShapes()
                .bakeModels(event.getModelLoader());
    }
}
