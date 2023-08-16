package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

import javax.annotation.Nonnull;

public class FluidOrbSpriteParticleProvider implements ParticleEngine.SpriteParticleRegistration<FluidOrbParticleOptions> {
    @Override
    @Nonnull
    public ParticleProvider<FluidOrbParticleOptions> create(@Nonnull SpriteSet pSprites) {
        return new FluidOrbParticleProvider(pSprites);
    }
}
