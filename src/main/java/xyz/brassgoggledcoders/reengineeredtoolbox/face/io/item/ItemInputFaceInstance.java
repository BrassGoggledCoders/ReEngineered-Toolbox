package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

public class ItemInputFaceInstance extends ItemIOFaceInstance {
    public ItemInputFaceInstance(Face face) {
        super(face, new PosInvHandler("Item Input", 80, 35, 1));
    }

    @Override
    public void onTick(ISocketTile tile) {

    }
}
