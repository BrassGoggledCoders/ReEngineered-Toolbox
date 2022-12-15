package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameGeometryLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate.PanelModelBakery;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerGeometryLoader(ModelEvent.RegisterGeometryLoaders registerGeometryLoaders) {
        registerGeometryLoaders.register(FrameGeometryLoader.NAME, new FrameGeometryLoader());
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.BakingCompleted registerAdditional) {
        PanelModelBakery.getInstance()
                .clear();
        PanelModelBakery.getInstance()
                .bake(registerAdditional.getModelBakery());
    }
}
