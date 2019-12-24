package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.impl.RedstoneSideSelector;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public class RedstoneOutputFaceInstance extends FaceInstance {
    private final RedstoneSideSelector sideSelector;

    public RedstoneOutputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.sideSelector = new RedstoneSideSelector(this, SelectorType.NONE, SelectorType.ACTIVE);
        Arrays.stream(Direction.values())
                .forEach(side -> this.sideSelector.setSelectorState(side, SelectorState.PULL));
    }

    @Override
    public boolean canConnectRedstone(ISocket socketTile, SocketContext socketContext) {
        return true;
    }

    @Override
    public int getStrongPower(@Nonnull ISocket socketTile, @Nullable SocketContext callerContext) {
        if (callerContext == null) {
            return Arrays.stream(Direction.values())
                    .map(socketTile::getFaceInstanceOnSide)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(faceInstance -> faceInstance.getStrongPower(socketTile, this.getSocketContext()))
                    .max(Integer::compareTo)
                    .orElse(0);
        } else {
            return 0;
        }
    }
}
