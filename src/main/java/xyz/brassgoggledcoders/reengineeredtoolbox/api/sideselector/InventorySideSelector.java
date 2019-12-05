package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.Range;

public class InventorySideSelector extends SideSelector<IItemHandler> {
    private final IItemHandler ownedItemHandler;
    private final Range<Integer> slotRange;

    public InventorySideSelector(IItemHandler ownedItemHandler, boolean allowPush, boolean allowPull) {
        this(ownedItemHandler, Range.between(0, ownedItemHandler.getSlots() - 1), allowPush, allowPull);
    }

    public InventorySideSelector(IItemHandler ownedItemHandler, Range<Integer> slotRange, boolean allowPush, boolean allowPull) {
        super(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, allowPush, allowPull);
        this.ownedItemHandler = ownedItemHandler;
        this.slotRange = slotRange;
    }

    @Override
    protected void handlePull(IItemHandler target) {

    }

    @Override
    protected void handlePush(IItemHandler target) {

    }
}
