package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

public class ItemOutputFaceInstance extends ItemIOFaceInstance {
    public ItemOutputFaceInstance(Face face) {
        super(face, new ItemStackHandler());
    }

    @Override
    public void onTick(ISocketTile tile) {

    }
}
