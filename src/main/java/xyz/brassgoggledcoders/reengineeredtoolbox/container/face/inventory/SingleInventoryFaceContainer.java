package xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public class SingleInventoryFaceContainer<T extends FaceInstance> implements IFaceContainer {
    private final T faceInstance;
    private final PosInvHandler posInvHandler;

    public SingleInventoryFaceContainer(T faceInstance, PosInvHandler posInvHandler) {
        this.posInvHandler = posInvHandler;
        this.faceInstance = faceInstance;
    }

    @Override
    public void setup(ISocketContainer container) {
        int i = 0;
        PosInvHandler handler = this.getPosInvHandler();
        for (int y = 0; y < handler.getYSize(); ++y) {
            for (int x = 0; x < handler.getXSize(); ++x) {
                container.addSlot(new SlotItemHandler(handler, i, handler.getXPos() +
                        handler.getSlotPosition().apply(i).getLeft(), handler.getYPos() +
                        handler.getSlotPosition().apply(i).getRight()));
                ++i;
            }
        }
    }

    protected PosInvHandler getPosInvHandler() {
        return posInvHandler;
    }

    protected T getFaceInstance() {
        return faceInstance;
    }
}
