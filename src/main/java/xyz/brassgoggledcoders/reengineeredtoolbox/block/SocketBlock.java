package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import javax.annotation.Nullable;

public class SocketBlock extends Block {
    public SocketBlock(Block.Properties properties) {
        super(properties);
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
}
