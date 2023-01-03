package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ServerConnectionTabManager;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.DEDICATED_SERVER, bus = Bus.FORGE)
public class ForgeServerEventHandler {
    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent serverTickEvent) {
        ServerConnectionTabManager.getInstance()
                .tick();
    }
}
