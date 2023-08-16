package xyz.brassgoggledcoders.reengineeredtoolbox.particle.fluidorb;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.ByteBufHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
public class FluidOrbParticleDeserializer implements ParticleOptions.Deserializer<FluidOrbParticleOptions> {
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public FluidOrbParticleOptions fromCommand(ParticleType<FluidOrbParticleOptions> pParticleType, StringReader pReader) throws CommandSyntaxException {
        pReader.expect(' ');
        String fluidName = pReader.readString();
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(pReader.readString()));
        if (fluid == null) {
            fluid = Fluids.WATER;
            ReEngineeredToolbox.LOGGER.error("Failed ot find fluid of name: %s".formatted(fluidName));
        }
        pReader.expect(' ');
        pReader.expect('(');
        Vec3 destination = new Vec3(
                pReader.readDouble(),
                pReader.readDouble(),
                pReader.readDouble()
        );
        pReader.expect(')');
        pReader.expect(' ');
        int lifetime = pReader.readInt();

        return new FluidOrbParticleOptions(
                fluid,
                destination,
                lifetime
        );
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public FluidOrbParticleOptions fromNetwork(ParticleType<FluidOrbParticleOptions> pParticleType, FriendlyByteBuf pBuffer) {
        return new FluidOrbParticleOptions(
                pBuffer.readRegistryId(),
                ByteBufHelper.readVector3d(pBuffer),
                pBuffer.readInt()
        );
    }
}
