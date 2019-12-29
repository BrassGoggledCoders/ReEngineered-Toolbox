package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCoreType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty.Empty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty.EmptyConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty.EmptyConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitType;

@SuppressWarnings("unused")
public class Conduits {
    //region Conduit Types
    private static final DeferredRegister<ConduitType<?, ?, ?>> CONDUIT_TYPE =
            new DeferredRegister<>(RETRegistries.CONDUIT_TYPES, ReEngineeredToolbox.ID);

    public static final RegistryObject<RedstoneConduitType> REDSTONE_TYPE = CONDUIT_TYPE.register("redstone",
            RedstoneConduitType::new);
    public static final RegistryObject<EnergyConduitType> ENERGY_TYPE = CONDUIT_TYPE.register("energy",
            EnergyConduitType::new);
    public static final RegistryObject<EmptyConduitType> EMPTY_TYPE = CONDUIT_TYPE.register("empty",
            EmptyConduitType::new);

    //endregion

    //region Conduit Core Types
    private static final DeferredRegister<ConduitCoreType<?, ?>> CONDUIT_CORE_TYPE =
            new DeferredRegister<>(RETRegistries.CONDUIT_CORE_TYPES, ReEngineeredToolbox.ID);

    public static final RegistryObject<ConduitCoreType<RedstoneConduitCore, RedstoneConduitType>> REDSTONE_CORE_TYPE =
            CONDUIT_CORE_TYPE.register("redstone", () -> new ConduitCoreType<>(REDSTONE_TYPE, RedstoneConduitCore::new));
    public static final RegistryObject<ConduitCoreType<EnergyConduitCore, EnergyConduitType>> ENERGY_CORE_TYPE =
            CONDUIT_CORE_TYPE.register("energy", () -> new ConduitCoreType<>(ENERGY_TYPE, EnergyConduitCore::new));
    public static final RegistryObject<ConduitCoreType<EmptyConduitCore<Empty, Empty, EmptyConduitType>, EmptyConduitType>> EMPTY_CORE_TYPE =
            CONDUIT_CORE_TYPE.register("empty", () -> new ConduitCoreType<>(EMPTY_TYPE,
                    () -> new EmptyConduitCore<>(RETObjects.EMPTY_TYPE.get(), Empty::new)));

    //endregion

    public static void register(IEventBus bus) {
        CONDUIT_TYPE.register(bus);
        CONDUIT_CORE_TYPE.register(bus);
    }
}
