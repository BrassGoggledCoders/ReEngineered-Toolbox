package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.impl.RedstoneSideSelector;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RedstoneOutputFaceInstance extends FaceInstance {
    private final RedstoneSideSelector sideSelector;

    public RedstoneOutputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.sideSelector = new RedstoneSideSelector(this, SelectorType.NONE, SelectorType.ACTIVE);
    }

    @Override
    public int getStrongPower(@Nonnull ISocketTile socketTile, @Nullable SocketContext callerContext) {
        if (callerContext == null) {
            return this.sideSelector.getValue(socketTile, true);
        } else {
            return 0;
        }
    }
}
