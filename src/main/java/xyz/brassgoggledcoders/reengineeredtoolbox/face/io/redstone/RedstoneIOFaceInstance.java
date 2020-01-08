package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.BasicFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.BasicFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RedstoneIOFaceInstance extends FaceInstance {
    private final RedstoneConduitClient redstoneConduitClient;

    public RedstoneIOFaceInstance(SocketContext socketContext, Function<FaceInstance, RedstoneConduitClient> redstoneConduitClient) {
        super(socketContext);
        this.redstoneConduitClient = redstoneConduitClient.apply(this);
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new BasicFaceScreen<>(this);
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new BasicFaceContainer<>(this);
    }

    @Override
    public List<ConduitClient<?, ?, ?>> getConduitClients() {
        return Collections.singletonList(redstoneConduitClient);
    }

    protected RedstoneConduitClient getRedstoneConduitClient() {
        return redstoneConduitClient;
    }
}
