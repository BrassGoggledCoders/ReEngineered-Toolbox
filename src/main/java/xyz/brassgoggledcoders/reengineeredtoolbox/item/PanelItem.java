package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.placement.ItemUsePanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class PanelItem extends Item {
    public PanelItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResultType useOn(@Nonnull ItemUseContext context) {
        TileEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (tileEntity instanceof IFrame) {
            if (((IFrame) tileEntity).placePanel(new ItemUsePanelPlacement(context))) {
                context.getItemInHand().shrink(1);
                return ActionResultType.sidedSuccess(context.getLevel().isClientSide);
            } else {
                return ActionResultType.FAIL;
            }
        }
        return super.useOn(context);
    }

    @Override
    @Nonnull
    public ITextComponent getName(@Nonnull ItemStack pStack) {
        CompoundNBT panelTag = pStack.getTagElement("panel");
        if (panelTag != null) {
            return NBTHelper.readPanelState(panelTag.getCompound("state"))
                    .getPanel()
                    .getName();
        }
        return super.getName(pStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> itemStacks) {
        if (this.allowdedIn(itemGroup)) {
            for (Panel panel : RETRegistries.PANELS.get()) {
                if (panel != RETPanels.OPEN.get()) {
                    ItemStack itemStack = new ItemStack(this);
                    itemStack.getOrCreateTagElement("panel")
                            .put("state", NBTHelper.writePanelState(panel.getDefaultState()));
                    itemStacks.add(itemStack);
                }
            }
        }
    }
}
