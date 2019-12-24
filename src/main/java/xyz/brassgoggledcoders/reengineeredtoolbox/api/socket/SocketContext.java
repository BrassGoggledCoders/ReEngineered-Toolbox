package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import javax.annotation.Nonnull;

public class SocketContext {
    private final Face face;
    private final Direction side;
    private final ISocket socket;

    public SocketContext(@Nonnull Face face, @Nonnull Direction side, @Nonnull ISocket socket) {
        this.face = face;
        this.side = side;
        this.socket = socket;
    }

    @Nonnull
    public Face getFace() {
        return face;
    }

    @Nonnull
    public Direction getSide() {
        return side;
    }

    @Nonnull
    public ISocket getSocket() {
        return socket;
    }
}
