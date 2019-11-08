package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.FaceHolder;

import java.lang.ref.WeakReference;

public class SocketFaceHolder extends FaceHolder {
    private final WeakReference<SocketTileEntity> socketReference;

    public SocketFaceHolder(WeakReference<SocketTileEntity> socketReference) {
        this.socketReference = socketReference;
    }

    @Override
    public void setFace(Face face) {
        super.setFace(face);
        SocketTileEntity tileEntity = socketReference.get();
        if (tileEntity != null) {
            tileEntity.updateFaces();
        }
    }
}
