package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

public class ItemInputFaceInstance extends ItemIOFaceInstance {
    public ItemInputFaceInstance(SocketContext socketContext) {
        super(socketContext, new PosInvHandler("Item Input", 80, 35, 1));
    }

    @Override
    public void onTick() {

    }
}
