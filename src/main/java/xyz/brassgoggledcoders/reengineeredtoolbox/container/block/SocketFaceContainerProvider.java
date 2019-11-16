package xyz.brassgoggledcoders.reengineeredtoolbox.container.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class SocketFaceContainerProvider implements INamedContainerProvider {
    private final SocketTileEntity tileEntity;
    private final Direction side;

    public SocketFaceContainerProvider(SocketTileEntity tileEntity, Direction side) {
        this.tileEntity = tileEntity;
        this.side = side;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return Optional.ofNullable(Blocks.SOCKET.get())
                .map(Block::getNameTextComponent)
                .orElseGet(() -> new StringTextComponent(""));
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SocketContainer(id, playerInventory, tileEntity, side);
    }
}
