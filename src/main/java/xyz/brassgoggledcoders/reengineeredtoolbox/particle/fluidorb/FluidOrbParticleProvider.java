package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class FluidOrbParticleProvider implements ParticleProvider<FluidOrbParticleOptions> {
    private final SpriteSet spriteSet;

    public FluidOrbParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Particle createParticle(FluidOrbParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        FluidOrbParticle particle = new FluidOrbParticle(pType, pLevel, pX, pY, pZ);
        particle.pickSprite(spriteSet);
        return particle;
    }
}
