package com.brassgoggledcoders.reengineeredtoolbox.block;

import com.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;
import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockSocket extends BlockTEBase<TileEntitySocket> {
    public BlockSocket() {
        super(Material.IRON, "socket");
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntitySocket();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntitySocket.class;
    }
}
