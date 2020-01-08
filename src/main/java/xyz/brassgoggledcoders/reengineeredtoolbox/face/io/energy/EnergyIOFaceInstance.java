package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy.EnergyConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.FaceContainerBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.component.energy.PosEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class EnergyIOFaceInstance extends FaceInstance implements IGuiAddonProvider {
    private final PosEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> externalOptional;
    private final EnergyConduitClient energyConduitClient;

    public EnergyIOFaceInstance(SocketContext socketContext, Function<IEnergyStorage, IEnergyStorage> externalLayer) {
        super(socketContext);
        this.energyStorage = new PosEnergyStorage(10000, 79, 24);
        this.energyConduitClient = createEnergyConduitClient(energyStorage);
        this.externalOptional = LazyOptional.of(() -> externalLayer.apply(energyStorage));
    }

    @Nonnull
    protected abstract EnergyConduitClient createEnergyConduitClient(IEnergyStorage energyStorage);

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            this.openScreen(player);
            return true;
        } else {
            return super.onActivated(player, hand, hit);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        energyStorage.deserializeNBT(nbt.getCompound("energyStorage"));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        compoundNBT.put("energyStorage", energyStorage.serializeNBT());
        return compoundNBT;
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
        return energyStorage.getGuiAddons();
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new FaceContainerBuilder()
                .withReferenceHolder(this.getEnergyStorage().getIntReferenceHolder())
                .finish();
    }

    @Override
    public Collection<ConduitClient<?, ?, ?>> getConduitClients() {
        return Collections.singleton(energyConduitClient);
    }

    public PosEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }
}
