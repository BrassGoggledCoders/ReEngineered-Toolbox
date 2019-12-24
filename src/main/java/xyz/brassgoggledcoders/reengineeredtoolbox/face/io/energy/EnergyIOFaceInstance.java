package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.BlankFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.energy.PosEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;

public class EnergyIOFaceInstance extends FaceInstance implements IGuiAddonProvider {
    private final PosEnergyStorage posEnergyStorage;
    private final LazyOptional<IEnergyStorage> externalOptional;

    public EnergyIOFaceInstance(SocketContext socketContext, Function<IEnergyStorage, IEnergyStorage> externalLayer) {
        super(socketContext);
        this.posEnergyStorage = new PosEnergyStorage(10000, 79, 24);
        this.externalOptional = LazyOptional.of(() -> externalLayer.apply(posEnergyStorage));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(ISocket tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            tile.openGui(player, this.getSocketContext());
        }
        return super.onActivated(tile, player, hand, hit);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            return externalOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return posEnergyStorage.getGuiAddons();
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new BlankFaceContainer();
    }
}
