package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;

public enum BlockPanelPosition implements IPanelPosition {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    private final Direction direction;

    BlockPanelPosition(Direction direction) {
        this.direction = direction;
    }

    @Override
    @NotNull
    public IPanelPosition getOpposite() {
        return fromDirection(this.direction.getOpposite());
    }

    @Override
    @NotNull
    public BlockPos offset(IFrameEntity frameEntity) {
        return frameEntity.getFramePos().relative(this.direction);
    }

    @Override
    @Nullable
    public Direction getFacing() {
        return this.direction;
    }

    public static BlockPanelPosition fromDirection(Direction direction) {
        return switch (direction) {
            case DOWN -> BlockPanelPosition.DOWN;
            case UP -> BlockPanelPosition.UP;
            case NORTH -> BlockPanelPosition.NORTH;
            case SOUTH -> BlockPanelPosition.SOUTH;
            case WEST -> BlockPanelPosition.WEST;
            case EAST -> BlockPanelPosition.EAST;
        };
    }
}
