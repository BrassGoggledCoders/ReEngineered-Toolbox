package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.capability.energy.EnergyStorageWrapper;

import javax.annotation.Nonnull;
import java.util.OptionalInt;

public class EnergyOutputFaceInstance extends EnergyIOFaceInstance {
    public EnergyOutputFaceInstance(SocketContext socketContext) {
        super(socketContext, energyStorage -> new EnergyStorageWrapper(true, false, energyStorage));
    }

    @Override
    @Nonnull
    protected EnergyConduitClient createEnergyConduitClient(IEnergyStorage energyStorage) {
        return EnergyConduitClient.createConsumer(this, this.getName(), context ->
                        OptionalInt.of(energyStorage.receiveEnergy(context.getAmount(), context.isSimulate()))
        );
    }
}
