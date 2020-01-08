package xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face;

import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.socket.ISocketContainer;

import java.util.List;

public class FaceContainerBuilt implements IFaceContainer {
    private final List<IntReferenceHolder> intReferenceHolders;
    private final List<IIntArray> arrayReferenceHolders;
    private final List<Slot> slots;

    FaceContainerBuilt(List<IntReferenceHolder> intReferenceHolders, List<IIntArray> arrayReferenceHolders,
                              List<Slot> slots) {
        this.intReferenceHolders = intReferenceHolders;
        this.arrayReferenceHolders = arrayReferenceHolders;
        this.slots = slots;
    }

    @Override
    public void setup(ISocketContainer container) {
        intReferenceHolders.forEach(container::trackInt);
        arrayReferenceHolders.forEach(container::trackIntArray);
        slots.forEach(container::addSlot);
    }
}
