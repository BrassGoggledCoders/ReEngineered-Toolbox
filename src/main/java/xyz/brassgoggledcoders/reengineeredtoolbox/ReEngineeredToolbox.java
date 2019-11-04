package xyz.brassgoggledcoders.reengineeredtoolbox;

import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Faces;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineeredtoolbox";

    public ReEngineeredToolbox() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        Faces.register(modBus);
    }

}
