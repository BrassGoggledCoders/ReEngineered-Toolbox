package xyz.brassgoggledcoders.reengineeredtoolbox.container.face.io;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.BasicFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy.EnergyIOFaceInstance;

import javax.annotation.Nonnull;

public class EnergyIOContainer extends BasicFaceContainer<EnergyIOFaceInstance> {
    public EnergyIOContainer(EnergyIOFaceInstance faceInstance) {
        super(faceInstance);
    }

    @Override
    public void setup(@Nonnull ISocketContainer container) {
        super.setup(container);
        container.trackInt(this.getFaceInstance().getEnergyStorage().getIntReferenceHolder());
    }
}
