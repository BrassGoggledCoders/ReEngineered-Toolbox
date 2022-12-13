package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class FrameBlock extends Block implements EntityBlock {
    public FrameBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ReEngineeredBlocks.IRON_FRAME_ENTITY.create(pPos, pState);
    }
}
