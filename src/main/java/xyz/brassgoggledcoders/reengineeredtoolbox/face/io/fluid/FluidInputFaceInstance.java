package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.capability.fluid.FluidHandlerWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

public class FluidInputFaceInstance extends FluidIOFaceInstance {
    public FluidInputFaceInstance(SocketContext context) {
        super(context, fluidHandler -> new FluidHandlerWrapper(fluidHandler, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (FluidUtil.interactWithFluidHandler(player, hand, this.getFluidHandlerWrapper())) {
            return true;
        }
        return super.onActivated(player, hand, hit);
    }
}
