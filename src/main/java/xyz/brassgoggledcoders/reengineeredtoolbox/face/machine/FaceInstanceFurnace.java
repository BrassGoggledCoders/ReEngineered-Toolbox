package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public class FaceInstanceFurnace extends FaceInstance {
    private int itemQueueInput = 0;
    private int itemQueueOutput = 1;

    private ItemStackHandler itemStackHandler = new ItemStackHandler(2);


}
