package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb.FluidOrbParticleOptions;
import xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb.FluidOrbParticleType;

public class ReEngineeredParticles {
    public static final RegistryEntry<ParticleType<FluidOrbParticleOptions>> FLUID_ORB =
            ReEngineeredToolbox.getRegistrate()
                    .object("fluid_orb")
                    .simple(Registry.PARTICLE_TYPE_REGISTRY, FluidOrbParticleType::new);

    public static void setup() {

    }
}
