package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class RedstoneOutputFaceInstance extends RedstoneIOFaceInstance {

    public RedstoneOutputFaceInstance(SocketContext socketContext) {
        super(socketContext, RedstoneOutputFaceInstance::createConsumer);
    }

    private static RedstoneConduitClient createConsumer(FaceInstance faceInstance) {
        return RedstoneConduitClient.createConsumer(faceInstance, faceInstance.getName());
    }

    @Override
    public int getStrongPower() {
        return this.getRedstoneConduitClient()
                .extractFrom(new RedstoneContext(true))
                .orElse(0);
    }
}
