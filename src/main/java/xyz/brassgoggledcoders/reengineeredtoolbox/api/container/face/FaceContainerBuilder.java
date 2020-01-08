package xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face;

import com.google.common.collect.Lists;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;

import java.util.Collection;
import java.util.List;

public class FaceContainerBuilder {
    private final List<IntReferenceHolder> intReferenceHolders;
    private final List<IIntArray> arrayReferenceHolders;
    private final List<Slot> slots;

    public FaceContainerBuilder() {
        intReferenceHolders = Lists.newArrayList();
        arrayReferenceHolders = Lists.newArrayList();
        slots = Lists.newArrayList();
    }

    public FaceContainerBuilder withReferenceHolder(IntReferenceHolder referenceHolder) {
        this.intReferenceHolders.add(referenceHolder);
        return this;
    }

    public FaceContainerBuilder withArrayReferenceHolder(IIntArray intArray) {
        this.arrayReferenceHolders.add(intArray);
        return this;
    }

    public FaceContainerBuilder withSlot(Slot slot) {
        this.slots.add(slot);
        return this;
    }

    public FaceContainerBuilder withReferenceHolders(Collection<IntReferenceHolder> referenceHolder) {
        this.intReferenceHolders.addAll(referenceHolder);
        return this;
    }

    public FaceContainerBuilder withArrayReferenceHolders(Collection<IIntArray> intArray) {
        this.arrayReferenceHolders.addAll(intArray);
        return this;
    }

    public FaceContainerBuilder withSlots(Collection<Slot> slot) {
        this.slots.addAll(slot);
        return this;
    }

    public FaceContainerBuilt finish() {
        return new FaceContainerBuilt(intReferenceHolders, arrayReferenceHolders, slots);
    }
}
