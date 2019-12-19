package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class FluidInputFaceInstance extends FluidIOFaceInstance {
    public FluidInputFaceInstance(SocketContext context) {
        super(context, new PosFluidTank("Fluid Input", 4000, 80, 28));
    }
}
