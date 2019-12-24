package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitType;

public class Conduits {
    public static final DeferredRegister<ConduitType<?, ?, ?, ?, ?>> CONDUIT_TYPE =
            new DeferredRegister<>(RETRegistries.CONDUITS, "reengineeredtoolbox");

    public static final RegistryObject<RedstoneConduitType> REDSTONE = CONDUIT_TYPE.register("redstone",
            RedstoneConduitType::new);

    public static void register(IEventBus bus) {
        CONDUIT_TYPE.register(bus);
    }
}
