package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.placement.ItemUsePanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class PanelItem<P extends Panel> extends Item {
    private final NonNullSupplier<P> panelSupplier;

    public PanelItem(NonNullSupplier<P> panelSupplier, Properties properties) {
        super(properties);
        this.panelSupplier = panelSupplier;
    }

    @Override
    @Nonnull
    public ActionResultType useOn(@Nonnull ItemUseContext context) {
        TileEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (tileEntity instanceof IFrame) {
            if (((IFrame) tileEntity).placePanel(new ItemUsePanelPlacement(context, panelSupplier.get()))) {
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
        return panelSupplier.get().getName();
    }

    public static <P2 extends Panel> PanelItem<P2> create(com.tterrag.registrate.util.nullness.NonNullSupplier<P2> supplier, Properties properties) {
        return new PanelItem<>(supplier::get, properties);
    }
}
