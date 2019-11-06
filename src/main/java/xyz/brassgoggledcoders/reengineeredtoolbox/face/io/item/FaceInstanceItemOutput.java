package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

public class FaceInstanceItemOutput extends FaceInstanceItemIO {
    public FaceInstanceItemOutput(Face face) {
        super(face, new ItemStackHandler());
    }

    @Override
    public void onTick(ISocketTile tile) {

    }
}
