package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class RETRegistries {
    static {
        init();
    }

    public static final IForgeRegistry<Face> FACES = RegistryManager.ACTIVE.getRegistry(Face.class);
    public static final IForgeRegistry<ConduitType<?, ?, ?>> CONDUITS = RegistryManager.ACTIVE.getRegistry(ConduitType.class);

    @SuppressWarnings("unchecked")
    private static void init() {
        makeRegistry(new ResourceLocation("reengineeredtoolbox", "faces"), Face.class)
                .setDefaultKey(new ResourceLocation("reengineeredtoolbox", "empty"))
                .create();
        makeRegistry(new ResourceLocation("reengineeredtoolbox", "conduits"), ConduitType.class)
                .create();
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type) {
        return new RegistryBuilder<T>()
                .setName(name)
                .setType(type);
    }
}
