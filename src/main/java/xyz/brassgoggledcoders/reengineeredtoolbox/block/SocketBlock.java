package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SocketBlock extends Block {
    public SocketBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return handleTile(world, pos, tileEntity -> tileEntity.onBlockActivated(player, hand, hit), false);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SocketTileEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        return handleTile(world, pos, SocketTileEntity::getComparatorSignal, 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getStrongPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return handleTile(blockReader, pos, tile -> tile.getStrongPower(side), 0);
    }

    @Override
    @SuppressWarnings("deprecation")

    public int getWeakPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return handleTile(blockReader, pos, tile -> tile.getWeakPower(side), 0);
    }

    private <T> T handleTile(IBlockReader reader, BlockPos blockPos, Function<SocketTileEntity, T> handleTile, T value) {
        TileEntity tileEntity = reader.getTileEntity(blockPos);
        if (tileEntity instanceof SocketTileEntity) {
            return handleTile.apply((SocketTileEntity) tileEntity);
        }
        return value;
    }
}
