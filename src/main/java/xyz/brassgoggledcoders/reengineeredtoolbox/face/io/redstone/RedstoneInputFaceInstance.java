package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.impl.RedstoneSideSelector;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RedstoneInputFaceInstance extends FaceInstance {
    private RedstoneSideSelector sideSelector;

    public RedstoneInputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.sideSelector = new RedstoneSideSelector(this, SelectorType.PASSIVE, SelectorType.NONE);
    }

    @Override
    public int getStrongPower(@Nonnull ISocketTile socketTile, @Nullable SocketContext callerContext) {
        if (callerContext != null) {
            return sideSelector.getPassive(socketTile, callerContext);
        } else {
            return 0;
        }
    }
}
