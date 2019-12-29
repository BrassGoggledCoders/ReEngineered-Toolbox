package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCoreType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty.EmptyConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.empty.EmptyConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitType;

public class RETObjects {
    public static final String modid = "reengineeredtoolbox";

    //region Conduit Types
    public static final RegistryObject<RedstoneConduitType> REDSTONE_TYPE =
            RegistryObject.of(make("redstone"), RETRegistries.CONDUIT_TYPES);
    public static final RegistryObject<EnergyConduitType> ENERGY_TYPE =
            RegistryObject.of(make("energy"), RETRegistries.CONDUIT_TYPES);
    public static final RegistryObject<EmptyConduitType> EMPTY_TYPE =
            RegistryObject.of(make("empty"), RETRegistries.CONDUIT_TYPES);
    //endregion

    //region Conduit Core Types
    public static final RegistryObject<ConduitCoreType<?, RedstoneConduitType>> REDSTONE_CORE_TYPE =
            RegistryObject.of(make("redstone"), RETRegistries.CONDUIT_CORE_TYPES);
    public static final RegistryObject<ConduitCoreType<?, EnergyConduitType>> ENERGY_CORE_TYPE =
            RegistryObject.of(make("energy"), RETRegistries.CONDUIT_CORE_TYPES);
    public static final RegistryObject<ConduitCoreType<EmptyConduitCore<?, ?, EmptyConduitType>, EmptyConduitType>> EMPTY_CORE_TYPE =
            RegistryObject.of(make("empty"), RETRegistries.CONDUIT_CORE_TYPES);
    //endregion

    private static ResourceLocation make(String path) {
        return new ResourceLocation(modid, path);
    }
}
