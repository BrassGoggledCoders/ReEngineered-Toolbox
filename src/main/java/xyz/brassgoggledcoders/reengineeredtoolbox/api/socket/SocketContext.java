package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class SocketContext {
    private final Face face;
    private final Direction side;

    public SocketContext(Face face, Direction side) {
        this.face = face;
        this.side = side;
    }

    public Face getFace() {
        return face;
    }

    public Direction getSide() {
        return side;
    }
}
