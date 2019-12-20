package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.energy.EnergyStorageWrapper;

public class EnergyOutputFaceInstance extends EnergyIOFaceInstance {
    public EnergyOutputFaceInstance(SocketContext socketContext) {
        super(socketContext, energyStorage -> new EnergyStorageWrapper(true, false, energyStorage));
    }
}
