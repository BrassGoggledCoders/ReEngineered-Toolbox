package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.energy.EnergyStorageWrapper;

public class EnergyInputFaceInstance extends EnergyIOFaceInstance {
    public EnergyInputFaceInstance(SocketContext socketContext) {
        super(socketContext, energyStorage -> new EnergyStorageWrapper(false, true, energyStorage));
    }
}
