package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.data.PanelModelBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.data.PanelModelProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameGeometryLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate.PanelModelBakery;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerGeometryLoader(ModelEvent.RegisterGeometryLoaders registerGeometryLoaders) {
        registerGeometryLoaders.register(FrameGeometryLoader.NAME, new FrameGeometryLoader());
    }

    @SubscribeEvent
    public static void registerPanelModels(ModelEvent.RegisterAdditional registerAdditional) {
        PanelModelBakery.getInstance()
                .clear();
        PanelModelBakery.getInstance()
                .registerPanelModels(registerAdditional::register);
    }

    @SubscribeEvent
    public static void bakePanelModels(ModelEvent.BakingCompleted registerAdditional) {

        PanelModelBakery.getInstance()
                .bake(registerAdditional.getModelBakery());
    }
}
