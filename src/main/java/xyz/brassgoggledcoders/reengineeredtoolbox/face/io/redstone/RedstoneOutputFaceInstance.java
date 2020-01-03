package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.Collection;
import java.util.Collections;

public class RedstoneOutputFaceInstance extends FaceInstance {
    private final RedstoneConduitClient redstoneConduitClient;

    public RedstoneOutputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.redstoneConduitClient = RedstoneConduitClient.createConsumer(this, this.getName());
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

    @Override
    public Collection<ConduitClient<?, ?, ?>> getConduitClients() {
        return Collections.singletonList(redstoneConduitClient);
    }
}
