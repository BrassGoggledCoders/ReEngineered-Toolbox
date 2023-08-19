package xyz.brassgoggledcoders.reengineeredtoolbox.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.BlockPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredBlocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredItemTags;

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
            IPanelPosition panelPosition = BlockPanelPosition.fromDirection(direction);
            PanelEntity entity = frameEntity.getPanelEntity(panelPosition);
            if (entity != null) {
                entity.neighborChanged();
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction != null && level.getBlockEntity(pos) instanceof IFrameEntity frameEntity) {
            IPanelPosition panelPosition = BlockPanelPosition.fromDirection(direction);
            return frameEntity.getPanelState(panelPosition)
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
            ItemStack heldItem = pPlayer.getItemInHand(pHand);
            IPanelPosition panelPosition = BlockPanelPosition.fromDirection(pHit.getDirection());
            if (heldItem.isEmpty() || heldItem.is(ReEngineeredItemTags.CAN_ALTER_FRAME_SLOT)) {
                if (frameEntity.changeFrameSlot(pHit, heldItem)) {
                    return InteractionResult.sidedSuccess(pLevel.isClientSide());
                }
            } else if (heldItem.is(ReEngineeredItemTags.CAN_REMOVE_PANEL)) {
                if (frameEntity.hasPanel(panelPosition)) {
                    frameEntity.removePanel(panelPosition, pPlayer, heldItem)
                            .forEach(itemStack -> popResource(pLevel, pPos, itemStack));

                    return InteractionResult.sidedSuccess(pLevel.isClientSide());
                }

            }


            return frameEntity.getPanelState(panelPosition)
                    .use(frameEntity, panelPosition, pPlayer, pHand, pHit);
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockAccess.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            IPanelPosition panelPosition = BlockPanelPosition.fromDirection(pSide.getOpposite());
            return frameEntity.getPanelState(panelPosition)
                    .getSignal(frameEntity);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockAccess.getBlockEntity(pPos) instanceof IFrameEntity frameEntity) {
            IPanelPosition panelPosition = BlockPanelPosition.fromDirection(pSide.getOpposite());
            return frameEntity.getPanelState(panelPosition)
                    .getSignal(frameEntity);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(@NotNull BlockState pState) {
        return true;
    }


    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings({"RedundantCast", "unchecked"})
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide() && ReEngineeredBlocks.IRON_FRAME_ENTITY.is(pBlockEntityType)) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<FrameBlockEntity>) (tickerLevel, pPos, pState1, pBlockEntity) -> pBlockEntity.serverTick();
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof FrameBlockEntity frameBlockEntity) {
            frameBlockEntity.doScheduledTick();
        }
    }
}
