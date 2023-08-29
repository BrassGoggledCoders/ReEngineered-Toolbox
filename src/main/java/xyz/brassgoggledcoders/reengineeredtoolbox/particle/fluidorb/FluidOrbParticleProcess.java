package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.util.VectorHelper;

import java.lang.ref.WeakReference;

public class FluidOrbParticleProcess {
    private final WeakReference<Entity> reference;
    private final long endTime;
    private final Vec3 destination;
    private final Fluid fluid;

    public FluidOrbParticleProcess(Entity reference, long endTime, Vec3 destination, Fluid fluid) {
        this.reference = new WeakReference<>(reference);
        this.endTime = endTime;
        this.destination = destination;
        this.fluid = fluid;
    }

    public boolean isValid() {
        Entity entity = this.reference.get();
        return entity != null && entity.isAlive() && entity.isAddedToWorld() && entity.getLevel().getGameTime() < endTime;
    }

    public void spawnParticles() {
        Entity entity = this.reference.get();
        if (entity != null && entity.getLevel() instanceof ServerLevel serverLevel && serverLevel.getRandom().nextBoolean()) {
            Vec3 startPos = VectorHelper.withRandomOffset(entity.getOnPos(), serverLevel.getRandom(), 1);
            long timeRemaining = endTime - serverLevel.getGameTime();
            serverLevel.sendParticles(
                    new FluidOrbParticleOptions(
                            fluid,
                            destination,
                            Math.min((int) timeRemaining, 30)
                    ),
                    startPos.x,
                    startPos.y,
                    startPos.z,
                    serverLevel.getRandom().nextInt(2) + 1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.15F
            );
        }

    }
}
