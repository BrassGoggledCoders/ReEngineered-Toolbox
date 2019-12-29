package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

public class RedstoneInputFaceInstance extends FaceInstance {
    private final RedstoneConduitClient redstoneConduitClient;

    public RedstoneInputFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.redstoneConduitClient = RedstoneConduitClient.createSupplier(
                socketContext.getFace().getName(),
                redstoneContext -> OptionalInt.of(this.getSocket().getWorld()
                        .getRedstonePower(this.getSocket().getBlockPos().offset(this.getSocketContext().getSide()), this.getSocketContext().getSide()))
        );
        socketContext.getSocket()
                .getConduitManager()
                .getCoresFor(RETObjects.REDSTONE_TYPE)
                .stream()
                .findFirst()
                .ifPresent(redstoneConduitClient::setConnectedCore);
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public List<ConduitClient<?, ?, ?>> getConduitClients() {
        return Collections.singletonList(redstoneConduitClient);
    }
}
