package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.placement;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NBTHelper;

import javax.annotation.Nullable;

public class ItemUsePanelPlacement implements IPanelPlacement {
    private final ItemUseContext context;

    public ItemUsePanelPlacement(ItemUseContext context) {
        this.context = context;
    }


    @Override
    public Direction getPlacementFace() {
        return context.getClickedFace();
    }

    @Override
    @Nullable
    public PanelState getPanelState() {
        ItemStack itemStack = context.getItemInHand();
        CompoundNBT nbt = itemStack.getTagElement("panel");
        if (nbt != null) {
            return NBTHelper.readPanelState(nbt.getCompound("state"));
        }
        return null;
    }

    @Nullable
    @Override
    public CompoundNBT getPanelEntityNBT() {
        ItemStack itemStack = context.getItemInHand();
        CompoundNBT nbt = itemStack.getTagElement("panel");
        if (nbt != null) {
            CompoundNBT panelEntityNBT = nbt.getCompound("entity");
            return panelEntityNBT.isEmpty() ? null : panelEntityNBT;
        }
        return null;
    }
}
