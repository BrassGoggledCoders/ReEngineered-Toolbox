package xyz.brassgoggledcoders.reengineeredtoolbox.container.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SocketFaceContainerProvider implements INamedContainerProvider {
    private final SocketTileEntity tileEntity;
    private final SocketContext socketContext;

    public SocketFaceContainerProvider(SocketTileEntity tileEntity, SocketContext socketContext) {
        this.tileEntity = tileEntity;
        this.socketContext = socketContext;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return socketContext.getFace().getName().applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SocketContainer(id, playerInventory, tileEntity, socketContext.getSide());
    }
}
