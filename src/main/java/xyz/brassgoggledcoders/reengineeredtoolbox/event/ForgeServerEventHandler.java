package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ServerConnectionTabManager;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, bus = Bus.FORGE)
public class ForgeServerEventHandler {
    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent serverTickEvent) {
        if (serverTickEvent.side == LogicalSide.SERVER && serverTickEvent.phase == TickEvent.Phase.START) {
            ServerConnectionTabManager.getInstance()
                    .tick();
        }
    }
}
