package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class FurnaceFaceInstance extends FaceInstance {
    private ItemStackHandler itemStackHandler = new ItemStackHandler(2);

    public FurnaceFaceInstance(SocketContext socketContext) {
        super(socketContext);
    }
}
