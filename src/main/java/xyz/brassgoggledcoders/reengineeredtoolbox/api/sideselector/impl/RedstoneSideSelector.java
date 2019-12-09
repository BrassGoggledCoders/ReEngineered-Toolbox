package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.impl;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SelectorType;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector.SideSelector;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class RedstoneSideSelector extends SideSelector<Integer> {
    private final FaceInstance faceInstance;

    public RedstoneSideSelector(FaceInstance faceInstance, SelectorType push, SelectorType pull) {
        super(faceInstance.getSocketContext(), push, pull);
        this.faceInstance = faceInstance;
    }

    @Override
    public Integer getValue(ISocketTile socketTile, boolean simulate) {
        if (this.getPull().isInternal()) {
            return this.getSelectorStates()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == SelectorState.PULL)
                    .map(entry -> socketTile.getFaceInstanceOnSide(entry.getKey())
                            .map(sideFaceInstance -> sideFaceInstance.getStrongPower(socketTile, this.getSocketContext()))
                            .orElse(0))
                    .max(Integer::compareTo)
                    .orElse(0);
        } else {
            return 0;
        }
    }

    @Override
    protected void activePull(ISocketTile socketTile, Direction targetSide) {

    }

    @Override
    protected void activePush(ISocketTile socketTile, Direction targetSide) {

    }

    @Override
    protected Integer passivePull(ISocketTile socketTile, SocketContext callerContext) {
        return faceInstance.getStrongPower(socketTile, null);
    }

    @Override
    protected Integer passivePush(ISocketTile socketTile, SocketContext callerContext) {
        return null;
    }
}
