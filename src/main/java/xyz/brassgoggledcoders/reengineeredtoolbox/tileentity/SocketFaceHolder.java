package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.FaceHolder;

import java.lang.ref.WeakReference;

public class SocketFaceHolder extends FaceHolder {
    private final WeakReference<SocketTileEntity> socketReference;
    private final Direction side;

    public SocketFaceHolder(WeakReference<SocketTileEntity> socketReference, Direction side) {
        this.socketReference = socketReference;
        this.side = side;
    }

    @Override
    public void setFace(Face face) {
        super.setFace(face);
        SocketTileEntity tileEntity = socketReference.get();
        if (tileEntity != null) {
            tileEntity.updateFaceInstance(face, side);
            tileEntity.updateFaces();
        }
    }
}
