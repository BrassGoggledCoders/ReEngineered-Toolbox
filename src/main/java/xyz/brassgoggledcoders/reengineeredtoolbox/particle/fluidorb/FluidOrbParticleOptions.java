package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredParticles;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.ByteBufHelper;

import javax.annotation.Nonnull;

public record FluidOrbParticleOptions(
        Fluid fluid,
        Vec3 destination,
        int lifetime
) implements ParticleOptions {
    @Override
    @Nonnull
    public ParticleType<?> getType() {
        return ReEngineeredParticles.FLUID_ORB.get();
    }

    @Override
    public void writeToNetwork(@Nonnull FriendlyByteBuf pBuffer) {
        pBuffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
        ByteBufHelper.writeVector3d(this.destination(), pBuffer);
        pBuffer.writeInt(this.lifetime());
    }

    @Override
    @Nonnull
    public String writeToString() {
        return ForgeRegistries.FLUIDS.getKey(this.fluid()) + " " + this.destination().toString() + " " + this.lifetime();
    }
}
