package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.capability.energy.EnergyStorageWrapper;

import javax.annotation.Nonnull;
import java.util.OptionalInt;

public class EnergyInputFaceInstance extends EnergyIOFaceInstance {
    public EnergyInputFaceInstance(SocketContext socketContext) {
        super(socketContext, energyStorage -> new EnergyStorageWrapper(false, true, energyStorage));
    }

    @Override
    @Nonnull
    protected EnergyConduitClient createEnergyConduitClient(IEnergyStorage energyStorage) {
        return EnergyConduitClient.createSupplier(this, this.getName(), context ->
                        OptionalInt.of(energyStorage.extractEnergy(context.getAmount(), context.isSimulate()))
        );
    }
}
