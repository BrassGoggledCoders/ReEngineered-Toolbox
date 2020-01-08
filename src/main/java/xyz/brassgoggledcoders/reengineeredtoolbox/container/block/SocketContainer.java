package xyz.brassgoggledcoders.reengineeredtoolbox.container.block;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.ContainerInventoryBase;
import com.hrznstudio.titanium.container.impl.DisableableItemHandlerSlot;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.socket.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.BasicFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SocketContainer extends ContainerInventoryBase implements ISocketContainer, ILocatable {
    @ObjectHolder(ReEngineeredToolbox.ID + ":socket")
    public static ContainerType<SocketContainer> type;

    private final FaceInstance faceInstance;
    private final IFaceContainer faceContainer;
    private final PlayerInventory playerInventory;
    private final ISocket socket;

    public SocketContainer(int id, PlayerInventory inventory, ISocket socket, UUID faceIdentifier) {
        super(Objects.requireNonNull(Blocks.SOCKET_CONTAINER.get()), inventory, id);
        this.socket = socket;
        this.faceInstance = socket.getFaceInstance(faceIdentifier);
        this.faceContainer = Optional.ofNullable(faceInstance.getContainer())
                .orElseGet(() -> new BasicFaceContainer<>(faceInstance));
        this.playerInventory = inventory;
        this.faceContainer.setup(this);

        this.addHotBar();
    }

    @Nullable
    public static SocketContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        TileEntity tileEntity = inventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos());
        if (tileEntity instanceof ISocket) {
            return new SocketContainer(id, inventory, (ISocket) tileEntity, packetBuffer.readUniqueId());
        }
        ReEngineeredToolbox.LOGGER.warn("Failed to find TileEntity for Container");
        return null;
    }

    private void addHotBar() {
        Point hotBarPoint = IAssetProvider.getAsset(this.getAssetProvider(), AssetTypes.BACKGROUND).getHotbarPosition();
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(this.getPlayerInventory(), k, hotBarPoint.x + k * 18, hotBarPoint.y));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return faceContainer.canInteractWith(player);
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
    @Nonnull
    public Slot addSlot(@Nonnull IItemHandler handler, int index, int xPos, int yPos) {
        return this.addSlot(new DisableableItemHandlerSlot(handler, index, xPos, yPos, this));
    }

    @Override
    @Nonnull
    public Container getContainer() {
        return this;
    }

    @Override
    @Nonnull
    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public ISocket getSocket() {
        return socket;
    }

    @Override
    public LocatorInstance getLocatorInstance() {
        return new TileEntityLocatorInstance(socket.getBlockPos());
    }

    @Override
    @Nonnull
    public IntReferenceHolder trackInt(@Nonnull IntReferenceHolder holder) {
        return super.trackInt(holder);
    }

    @Override
    public void trackIntArray(@Nonnull IIntArray holders) {
        super.trackIntArray(holders);
    }
}
