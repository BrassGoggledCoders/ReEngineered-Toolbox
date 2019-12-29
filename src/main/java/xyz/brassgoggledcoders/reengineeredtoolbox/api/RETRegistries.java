package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCoreType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class RETRegistries {
    static {
        init();
    }

    public static final IForgeRegistry<Face> FACES = RegistryManager.ACTIVE.getRegistry(Face.class);
    public static final IForgeRegistry<ConduitType<?, ?, ?>> CONDUIT_TYPES =
            RegistryManager.ACTIVE.getRegistry(ConduitType.class);
    public static final IForgeRegistry<ConduitCoreType<?, ?>> CONDUIT_CORE_TYPES =
            RegistryManager.ACTIVE.getRegistry(ConduitCoreType.class);

    @SuppressWarnings("unchecked")
    private static void init() {
        makeRegistry(new ResourceLocation("reengineeredtoolbox", "faces"), Face.class)
                .create();
        makeRegistry(new ResourceLocation("reengineeredtoolbox", "conduit_types"), ConduitType.class)
                .create();
        makeRegistry(new ResourceLocation("reengineeredtoolbox", "conduit_core_types"), ConduitCoreType.class)
                .create();
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type) {
        return new RegistryBuilder<T>()
                .setName(name)
                .setType(type);
    }
}
