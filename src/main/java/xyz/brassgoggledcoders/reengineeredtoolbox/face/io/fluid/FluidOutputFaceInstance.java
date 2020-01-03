package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.capability.fluid.FluidHandlerWrapper;

public class FluidOutputFaceInstance extends FluidIOFaceInstance {
    public FluidOutputFaceInstance(SocketContext context) {
        super(context, fluidHandler -> new FluidHandlerWrapper(fluidHandler, false, true));
    }
}
