package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public class BasicFaceScreen<T extends FaceInstance> implements IFaceScreen {
    private final T faceInstance;

    public BasicFaceScreen(T faceInstance) {
        this.faceInstance = faceInstance;
    }

    protected T getFaceInstance() {
        return this.faceInstance;
    }
}
