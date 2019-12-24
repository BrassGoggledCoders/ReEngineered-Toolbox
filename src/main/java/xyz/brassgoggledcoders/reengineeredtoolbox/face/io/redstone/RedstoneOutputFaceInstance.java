package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class RedstoneOutputFaceInstance extends FaceInstance {
    private final RedstoneConduitClient redstoneConduitClient;

    public RedstoneOutputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.redstoneConduitClient = RedstoneConduitClient.createConsumer();
        socketContext.getSocket()
                .getConduitManager()
                .getCoresFor(Conduits.REDSTONE.get())
                .stream()
                .findFirst()
                .ifPresent(redstoneConduitClient::setConnectedCore);
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public int getStrongPower() {
        return redstoneConduitClient.extractFrom(new RedstoneContext(true))
                .orElse(0);
    }
}
