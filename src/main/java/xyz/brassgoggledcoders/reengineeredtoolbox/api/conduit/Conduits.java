package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitType;

public class Conduits {
    public static final DeferredRegister<ConduitType<?, ?, ?>> CONDUIT_TYPE =
            new DeferredRegister<>(RETRegistries.CONDUIT_TYPES, "reengineeredtoolbox");
    public static final DeferredRegister<ConduitCoreType<?, ?>> CONDUIT_CORE_TYPE =
            new DeferredRegister<>(RETRegistries.CONDUIT_CORE_TYPES, "reengineeredtoolbox");

    public static final RegistryObject<RedstoneConduitType> REDSTONE_TYPE = CONDUIT_TYPE.register("redstone",
            RedstoneConduitType::new);
    public static final RegistryObject<ConduitCoreType<RedstoneConduitCore, RedstoneConduitType>> REDSTONE_CORE_TYPE =
            CONDUIT_CORE_TYPE.register("redstone", () -> new ConduitCoreType<>(REDSTONE_TYPE, RedstoneConduitCore::new));

    public static void register(IEventBus bus) {
        CONDUIT_TYPE.register(bus);
    }
}
