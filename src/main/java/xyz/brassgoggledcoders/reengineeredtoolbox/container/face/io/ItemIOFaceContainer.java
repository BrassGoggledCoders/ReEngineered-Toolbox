package xyz.brassgoggledcoders.reengineeredtoolbox.container.face.io;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemIOFaceInstance;

public class ItemIOFaceContainer implements IFaceContainer {
    private final ItemIOFaceInstance faceInstance;

    public ItemIOFaceContainer(ItemIOFaceInstance faceInstance) {
        this.faceInstance = faceInstance;
    }

    @Override
    public void setup(ISocketContainer container) {
        int i = 0;
        PosInvHandler handler = faceInstance.getInventory();
        for (int y = 0; y < handler.getYSize(); ++y) {
            for (int x = 0; x < handler.getXSize(); ++x) {
                container.addSlot(new SlotItemHandler(handler, i, handler.getXPos() +
                        handler.getSlotPosition().apply(i).getLeft(), handler.getYPos() +
                        handler.getSlotPosition().apply(i).getRight()));
                ++i;
            }
        }
    }
}
