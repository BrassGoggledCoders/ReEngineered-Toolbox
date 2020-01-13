package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.UUID;

public class FaceInstance implements INBTSerializable<CompoundNBT> {
    private final SocketContext socketContext;
    private final Map<String, ConduitClient<?, ?, ?>> conduitClients;
    private UUID uuid;

    public FaceInstance(SocketContext socketContext) {
        this.socketContext = socketContext;
        this.uuid = UUID.randomUUID();
        this.conduitClients = Maps.newHashMap();
    }

    public void onTick() {

    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uuid", this.uuid);
        if (!this.getConduitClients().isEmpty()) {
            CompoundNBT conduitClientsNBT = new CompoundNBT();
            for (Map.Entry<String, ConduitClient<?, ?, ?>> entry : this.getConduitClients().entrySet()) {
                conduitClientsNBT.put(entry.getKey(), entry.getValue().serializeNBT());
            }
            nbt.put("conduitClients", conduitClientsNBT);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("uuid")) {
            this.uuid = nbt.getUniqueId("uuid");
        }
        if (nbt.contains("conduitClients")) {
            CompoundNBT conduitClientsNBT = nbt.getCompound("conduitClients");
            for (Map.Entry<String, ConduitClient<?, ?, ?>> entry : this.getConduitClients().entrySet()) {
                if (conduitClientsNBT.contains(entry.getKey())) {
                    entry.getValue().deserializeNBT(conduitClientsNBT.getCompound(entry.getKey()));
                }
            }
        }
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
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
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

    public Map<String, ConduitClient<?, ?, ?>> getConduitClients() {
        return conduitClients;
    }

    protected void registerClient(String name, ConduitClient<?, ?, ?> conduitClient) {
        this.conduitClients.put(name, conduitClient);
    }

    protected void registerClient(ConduitClient<?, ?, ?> conduitClient) {
        this.registerClient(conduitClient.getConduitType().getDefaultClientName(), conduitClient);
    }

    protected void openScreen(PlayerEntity playerEntity) {
        this.getSocket().openScreen(playerEntity, this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ITextComponent getName() {
        return this.getFace().getName();
    }
}
