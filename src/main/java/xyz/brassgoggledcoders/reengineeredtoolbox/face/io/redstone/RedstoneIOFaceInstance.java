package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.BasicFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.BasicFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public class RedstoneIOFaceInstance extends FaceInstance {
    private final RedstoneConduitClient redstoneConduitClient;

    public RedstoneIOFaceInstance(SocketContext socketContext, Function<FaceInstance, RedstoneConduitClient> redstoneConduitClient) {
        super(socketContext);
        this.redstoneConduitClient = redstoneConduitClient.apply(this);
        this.registerClient(this.redstoneConduitClient);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            this.openScreen(player);
        }
        return super.onActivated(player, hand, hit);
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

    protected RedstoneConduitClient getRedstoneConduitClient() {
        return redstoneConduitClient;
    }
}
