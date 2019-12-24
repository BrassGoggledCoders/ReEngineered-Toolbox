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

public class RedstoneInputFaceInstance extends FaceInstance {
    private final RedstoneSideSelector sideSelector;

    public RedstoneInputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.sideSelector = new RedstoneSideSelector(this, SelectorType.NONE, SelectorType.PASSIVE);
        Arrays.stream(Direction.values())
                .forEach(side -> this.sideSelector.setSelectorState(side, SelectorState.PULL));
    }

    @Override
    public boolean canConnectRedstone(ISocket socketTile, SocketContext socketContext) {
        return true;
    }

    @Override
    public int getStrongPower(@Nonnull ISocket socketTile, @Nullable SocketContext callerContext) {
        if (callerContext != null) {
            return socketTile.getWorld().getRedstonePower(socketTile.getBlockPos().offset(this.getSocketContext().getSide()),
                    this.getSocketContext().getSide());
        } else {
            return 0;
        }
    }
}
