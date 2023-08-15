package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;

public interface IPanelPosition {
    IPanelPosition NONE = new IPanelPosition() {
        @Override
        @NotNull
        public IPanelPosition getOpposite() {
            return NONE;
        }

        @Override
        @NotNull
        public BlockPos offset(IFrameEntity frameEntity) {
            return frameEntity.getFramePos();
        }

        @Override
        @Nullable
        public Direction getFacing() {
            return null;
        }
    };

    @NotNull
    IPanelPosition getOpposite();

    @NotNull
    BlockPos offset(IFrameEntity frameEntity);

    @Nullable
    Direction getFacing();
}
