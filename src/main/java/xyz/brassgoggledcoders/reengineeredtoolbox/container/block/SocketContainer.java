package xyz.brassgoggledcoders.reengineeredtoolbox.container.block;

import com.hrznstudio.titanium.container.TitaniumContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.BlankFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class SocketContainer extends TitaniumContainerBase implements ISocketContainer {
    @ObjectHolder(ReEngineeredToolbox.ID + ":socket")
    public static ContainerType<SocketContainer> type;

    private final SocketTileEntity socketTileEntity;
    private final FaceInstance faceInstance;
    private final PlayerInventory playerInventory;
    private final IFaceContainer faceContainer;

    public SocketContainer(int id, PlayerInventory inventory, SocketTileEntity socketTileEntity, Direction sideOpened) {
        super(Objects.requireNonNull(Blocks.SOCKET_CONTAINER.get()), id);
        this.socketTileEntity = socketTileEntity;
        this.faceInstance = socketTileEntity.getFaceInstance(sideOpened);
        this.faceContainer = Optional.ofNullable(faceInstance.getContainer(this))
                .orElseGet(BlankFaceContainer::new);
        this.playerInventory = inventory;
        this.faceContainer.setup(this);
    }

    @Nullable
    public static SocketContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        TileEntity tileEntity = inventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos());
        if (tileEntity instanceof SocketTileEntity) {
            return new SocketContainer(id, inventory, (SocketTileEntity) tileEntity,
                    Direction.byName(packetBuffer.readString()));
        }
        ReEngineeredToolbox.LOGGER.warn("Failed to find TileEntity for Container");
        return null;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return faceContainer.canInteractWith(player);
    }

    public SocketTileEntity getSocketTileEntity() {
        return socketTileEntity;
    }

    public FaceInstance getFaceInstance() {
        return this.faceInstance;
    }

    @Override
    @Nonnull
    public Slot addSlot(@Nonnull Slot slot) {
        return super.addSlot(slot);
    }

    @Override
    public Container getContainer() {
        return this;
    }
}
