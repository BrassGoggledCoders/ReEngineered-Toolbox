package xyz.brassgoggledcoders.reengineeredtoolbox.container.face.io;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid.FluidIOFaceInstance;

public class FluidIOFaceContainer implements IFaceContainer {
    private final FluidIOFaceInstance fluidIOFaceInstance;

    public FluidIOFaceContainer(FluidIOFaceInstance fluidIOFaceInstance) {
        this.fluidIOFaceInstance = fluidIOFaceInstance;
    }

    @Override
    public void setup(ISocketContainer container) {

    }
}
