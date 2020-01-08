package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.container.impl.DisableableSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class Slots {
    public static List<Slot> getFromInventories(PosInvHandler... inventoryHandlers) {
        List<Slot> slots = Lists.newArrayList();
        for (PosInvHandler handler : inventoryHandlers) {
            int i = 0;
            for (int y = 0; y < handler.getYSize(); ++y) {
                for (int x = 0; x < handler.getXSize(); ++x) {
                    slots.add(new SlotItemHandler(handler, i, handler.getXPos() +
                            handler.getSlotPosition().apply(i).getLeft(), handler.getYPos() +
                            handler.getSlotPosition().apply(i).getRight()));
                    ++i;
                }
            }
        }
        return slots;
    }
}
