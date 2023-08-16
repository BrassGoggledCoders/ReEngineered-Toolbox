package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.Codecs;

public class FluidOrbParticleType extends ParticleType<FluidOrbParticleOptions> {
    public static final Codec<FluidOrbParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.FLUIDS.getCodec().fieldOf("fluid").forGetter(FluidOrbParticleOptions::fluid),
            Codecs.VECTOR.fieldOf("destination").forGetter(FluidOrbParticleOptions::destination),
            Codec.INT.fieldOf("lifetime").forGetter(FluidOrbParticleOptions::lifetime)
    ).apply(instance, FluidOrbParticleOptions::new));

    public FluidOrbParticleType() {
        super(false, new FluidOrbParticleDeserializer());
    }

    @Override
    @NotNull
    public Codec<FluidOrbParticleOptions> codec() {
        return CODEC;
    }
}
