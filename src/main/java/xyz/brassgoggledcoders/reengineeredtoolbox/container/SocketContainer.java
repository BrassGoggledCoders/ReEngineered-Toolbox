package xyz.brassgoggledcoders.reengineeredtoolbox.container;

import com.hrznstudio.titanium.container.TitaniumContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.registries.ObjectHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public class SocketContainer extends TitaniumContainerBase {
    @ObjectHolder(ReEngineeredToolbox.ID + ":socket")
    public static ContainerType<SocketContainer> type;

    private final SocketTileEntity socketTileEntity;

    public SocketContainer(int id, PlayerInventory inventory, SocketTileEntity socketTileEntity) {
        super(Objects.requireNonNull(Blocks.SOCKET_CONTAINER.get()), id);
        this.socketTileEntity = socketTileEntity;
    }

    @Nullable
    public static SocketContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        TileEntity tileEntity = inventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos());
        if (tileEntity instanceof SocketTileEntity) {
            return new SocketContainer(id, inventory, (SocketTileEntity) tileEntity);
        }
        ReEngineeredToolbox.LOGGER.warn("Failed to find TileEntity for Container");
        return null;
    }

    public SocketTileEntity getSocketTileEntity() {
        return socketTileEntity;
    }
}
