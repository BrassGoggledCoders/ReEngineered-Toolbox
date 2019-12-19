package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class FluidOutputFaceInstance extends FluidIOFaceInstance {
    public FluidOutputFaceInstance(SocketContext context) {
        super(context, new PosFluidTank("Fluid Output", 4000, 80, 35));
    }
}
