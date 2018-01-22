package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import com.google.common.collect.Lists;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.IGeneratedModel;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

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

    @Override
    public List<IGeneratedModel> getGeneratedModels() {
        return Lists.newArrayList();
    }
}
