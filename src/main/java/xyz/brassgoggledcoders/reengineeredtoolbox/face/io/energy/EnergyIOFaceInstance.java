package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.energy.PosEnergyStorage;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class EnergyIOFaceInstance extends FaceInstance {
    private final PosEnergyStorage posEnergyStorage;
    private final LazyOptional<IEnergyStorage> externalOptional;

    public EnergyIOFaceInstance(SocketContext socketContext, Function<IEnergyStorage, IEnergyStorage> externalLayer) {
        super(socketContext);
        this.posEnergyStorage = new PosEnergyStorage(10000, 54, 54);
        this.externalOptional = LazyOptional.of(() -> externalLayer.apply(posEnergyStorage));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            return externalOptional.cast();
        }
        return super.getCapability(cap);
    }
}
