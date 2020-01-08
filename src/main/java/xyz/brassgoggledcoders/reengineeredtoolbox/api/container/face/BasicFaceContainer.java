package xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.socket.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;

public class BasicFaceContainer<T extends FaceInstance> implements IFaceContainer {
    private final T faceInstance;

    public BasicFaceContainer(T faceInstance) {
        this.faceInstance = faceInstance;
    }

    @Override
    public void setup(@Nonnull ISocketContainer container) {

    }

    protected T getFaceInstance() {
        return faceInstance;
    }
}
