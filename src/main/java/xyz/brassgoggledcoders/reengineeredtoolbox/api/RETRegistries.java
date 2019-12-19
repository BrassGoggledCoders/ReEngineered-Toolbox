package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class RETRegistries {
    public final static IForgeRegistry<Face> FACES = new RegistryBuilder<Face>()
            .setName(new ResourceLocation("reengineeredtoolbox", "faces"))
            .setType(Face.class)
            .setDefaultKey(new ResourceLocation("reengineeredtoolbox", "empty"))
            .create();
}
