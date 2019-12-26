package xyz.brassgoggledcoders.reengineeredtoolbox.container.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SocketContainerProvider implements INamedContainerProvider {
    private final SocketTileEntity socket;
    private final FaceInstance faceInstance;

    public SocketContainerProvider(SocketTileEntity socket, FaceInstance faceInstance) {
        this.socket = socket;
        this.faceInstance = faceInstance;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return faceInstance.getFace().getName().applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SocketContainer(id, playerInventory, socket, faceInstance.getUuid());
    }
}
