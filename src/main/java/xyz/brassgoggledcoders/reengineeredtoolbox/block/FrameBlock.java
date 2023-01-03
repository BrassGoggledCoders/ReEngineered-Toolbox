package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
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

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        Direction direction = Direction.fromNormal(pFromPos.subtract(pPos));
        if (direction != null && pLevel.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            PanelEntity entity = frameEntity.getPanelEntity(direction);
            if (entity != null) {
                entity.neighborChanged();
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction != null && level.getBlockEntity(pos) instanceof IFrameEntity frameEntity) {
            return frameEntity.getPanelState(direction)
                    .canConnectRedstone(frameEntity);
        }
        return true;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            return frameEntity.getPanelState(pHit.getDirection())
                    .use(frameEntity, pPlayer, pHand, pHit);
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockAccess.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            return frameEntity.getPanelState(pSide.getOpposite())
                    .getSignal(frameEntity);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockAccess.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            return frameEntity.getPanelState(pSide.getOpposite())
                    .getSignal(frameEntity);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(@NotNull BlockState pState) {
        return true;
    }
}
