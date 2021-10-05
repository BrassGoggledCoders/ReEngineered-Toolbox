package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.placement;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NBTHelper;

import javax.annotation.Nullable;

public class ItemUsePanelPlacement implements IPanelPlacement {
    private final ItemUseContext context;
    private final Panel panel;

    public ItemUsePanelPlacement(ItemUseContext context, Panel panel) {
        this.context = context;
        this.panel = panel;
    }


    @Override
    public Direction getPlacementFace() {
        return context.getClickedFace();
    }

    @Override
    @Nullable
    public PanelState getPanelState() {
        ItemStack itemStack = context.getItemInHand();
        CompoundNBT nbt = itemStack.getTagElement("panelInfo");
        if (nbt != null) {
            return NBTHelper.readPanelState(nbt.getCompound("panelState"));
        }
        return panel.getDefaultState();
    }

    @Nullable
    @Override
    public CompoundNBT getPanelEntityNBT() {
        ItemStack itemStack = context.getItemInHand();
        CompoundNBT nbt = itemStack.getTagElement("panelInfo");
        if (nbt != null) {
            CompoundNBT panelEntityNBT = nbt.getCompound("panelEntity");
            return panelEntityNBT.isEmpty() ? null : panelEntityNBT;
        }
        return null;
    }
}
