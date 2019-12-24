package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

public class FaceInstance implements INBTSerializable<CompoundNBT> {
    private final SocketContext socketContext;

    public FaceInstance(SocketContext socketContext) {
        this.socketContext = socketContext;
    }

    public void onTick() {

    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public SocketContext getSocketContext() {
        return this.socketContext;
    }

    public Face getFace() {
        return this.getSocketContext().getFace();
    }

    public ISocket getSocket() {
        return this.getSocketContext().getSocket();
    }

    public World getWorld() {
        return this.getSocket().getWorld();
    }

    public ResourceLocation getSpriteLocation() {
        return this.getFace().getDefaultSpriteLocation();
    }

    public CompoundNBT getUpdateTag() {
        return new CompoundNBT();
    }

    public void handleUpdateTag(CompoundNBT updateNBT) {

    }

    @ParametersAreNonnullByDefault
    public boolean onActivated(ISocket tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return false;
    }

    @Nullable
    public IFaceContainer getContainer() {
        return null;
    }

    @Nullable
    public IFaceScreen getScreen() {
        return null;
    }

    /**
     * This call isn't side specific so the block will output based on highest strength through all faces
     *
     * @return The Strength of signal that a Comparator should output
     */
    public int getComparatorStrength() {
        return 0;
    }

    public int getStrongPower() {
        return 0;
    }

    public boolean canConnectRedstone() {
        return false;
    }

    public List<ConduitClient<?, ?, ?>> getConduitClients() {
        return Collections.emptyList();
    }
}
