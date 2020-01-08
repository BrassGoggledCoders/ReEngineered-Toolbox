package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.OptionalInt;

public class RedstoneInputFaceInstance extends RedstoneIOFaceInstance {
    public RedstoneInputFaceInstance(SocketContext socketContext) {
        super(socketContext, RedstoneInputFaceInstance::createSupplier);
    }

    private static RedstoneConduitClient createSupplier(FaceInstance faceInstance) {
        return RedstoneConduitClient.createSupplier(
                faceInstance, faceInstance.getName(),
                redstoneContext -> getPower(faceInstance.getSocket(), faceInstance.getSocketContext().getSide())
        );
    }

    private static OptionalInt getPower(ISocket socket, Direction side) {
        return OptionalInt.of(socket.getWorld().getRedstonePower(socket.getBlockPos().offset(side), side));
    }
}
