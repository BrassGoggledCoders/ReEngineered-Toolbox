package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;

import javax.annotation.Nonnull;

public class BlockSocket extends BlockTEBase<TileEntitySocket> {
    public static final SidedFacesUnlistedProperty SIDED_FACE_PROPERTY = new SidedFacesUnlistedProperty();

    public BlockSocket() {
        super(Material.IRON, "socket");
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{SIDED_FACE_PROPERTY});
    }

    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntitySocket();
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedState = (IExtendedBlockState) state;

        Optional<TileEntitySocket> socket = this.getTileEntity(world, pos);
        if (socket.isPresent()) {
            extendedState = socket.get().setExtendedState(extendedState);
        }

        return extendedState;
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
