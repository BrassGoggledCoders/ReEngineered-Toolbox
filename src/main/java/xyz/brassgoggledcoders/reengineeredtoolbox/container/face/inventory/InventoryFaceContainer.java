package xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public class InventoryFaceContainer<T extends FaceInstance> implements IFaceContainer {
    private final T faceInstance;
    private final PosInvHandler[] posInvHandlers;

    public InventoryFaceContainer(T faceInstance, PosInvHandler... posInvHandlers) {
        this.posInvHandlers = posInvHandlers;
        this.faceInstance = faceInstance;
    }

    @Override
    public void setup(ISocketContainer container) {
        PosInvHandler[] handlers = this.getPosInvHandlers();
        for (PosInvHandler handler : handlers) {
            int i = 0;
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

    protected PosInvHandler[] getPosInvHandlers() {
        return posInvHandlers;
    }

    protected T getFaceInstance() {
        return faceInstance;
    }
}
