package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

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
                this, this.getName(),
                redstoneContext -> OptionalInt.of(this.getSocket().getWorld()
                        .getRedstonePower(this.getSocket().getBlockPos().offset(this.getSocketContext().getSide()), this.getSocketContext().getSide()))
        );
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
