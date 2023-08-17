package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class FluidOrbParticle extends TextureSheetParticle {
    private final Vec3 destination;

    public FluidOrbParticle(FluidOrbParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);

        int color = IClientFluidTypeExtensions.of(options.fluid())
                .getTintColor();
        int r1 = (color >> 16) & 0xFF;
        int g1 = (color >> 8) & 0xFF;
        int b1 = color & 0xFF;
        this.setColor(
                r1 / 256F,
                g1 / 256F,
                b1 / 256F
        );

        this.destination = options.destination();
        this.lifetime = options.lifetime();
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            Vec3 currentPos = new Vec3(this.x, this.y, this.z);
            Vec3 direction = destination.subtract(currentPos).normalize();

            this.xd = direction.x / 8;
            this.yd = direction.y / 8;
            this.zd = direction.z / 8;
            this.move(xd, yd, zd);
            if (currentPos.closerThan(direction, 0.1)) {
                this.remove();
            }
        }
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
