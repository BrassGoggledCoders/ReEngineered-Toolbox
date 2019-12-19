package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class ItemOutputFaceInstance extends ItemIOFaceInstance {
    public ItemOutputFaceInstance(SocketContext socketContext) {
        super(socketContext, new PosInvHandler("Item Output", 80, 35, 1));
    }

    @Override
    public void onTick(ISocketTile tile) {

    }
}
