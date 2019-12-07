package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.dispenser;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;

public class FakeDispenserBlockSource implements IBlockSource {
    private final BlockState dispenserBlockState;
    private final DispenserTileEntity dispenserTileEntity;
    private ISocketTile socketTile;

    public FakeDispenserBlockSource(SocketContext socketContext) {
        this.dispenserBlockState = Blocks.DISPENSER.getDefaultState()
                .with(DispenserBlock.FACING, socketContext.getSide());
        this.dispenserTileEntity = new DispenserTileEntity();
    }

    @Override
    public double getX() {
        return socketTile.getBlockPos().getX() + 0.5D;
    }

    @Override
    public double getY() {
        return socketTile.getBlockPos().getY() + 0.5D;
    }

    @Override
    public double getZ() {
        return socketTile.getBlockPos().getZ() + 0.5D;
    }

    @Override
    @Nonnull
    public BlockPos getBlockPos() {
        return socketTile.getBlockPos();
    }

    @Override
    @Nonnull
    public BlockState getBlockState() {
        return dispenserBlockState;
    }

    @Override
    @Nonnull
    @SuppressWarnings("unchecked")
    public <T extends TileEntity> T getBlockTileEntity() {
        return (T) dispenserTileEntity;
    }

    @Override
    @Nonnull
    public World getWorld() {
        return socketTile.getWorld();
    }

    public void setSocketTile(ISocketTile socketTile) {
        this.socketTile = socketTile;
        if (socketTile != null) {
            dispenserTileEntity.setWorld(socketTile.getWorld());
            dispenserTileEntity.setPos(socketTile.getBlockPos());
        }
    }
}
