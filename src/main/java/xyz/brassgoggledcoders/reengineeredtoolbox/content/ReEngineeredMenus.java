package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.ItemIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.RedstoneIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;

@SuppressWarnings("RedundantTypeArguments")
public class ReEngineeredMenus {

    public static final MenuEntry<RedstoneIOPanelMenu> REDSTONE_IO = ReEngineeredToolbox.getRegistrate()
            .object("redstone_io")
            .<RedstoneIOPanelMenu, SingleTypedSlotScreen<RedstoneIOPanelMenu, IRedstoneTypedSlot, Integer>>menu(
                    RedstoneIOPanelMenu::create,
                    () -> SingleTypedSlotScreen::create
            )
            .register();

    public static final MenuEntry<ItemIOPanelMenu> ITEM_IO = ReEngineeredToolbox.getRegistrate()
            .object("item_io")
            .<ItemIOPanelMenu, SingleTypedSlotScreen<ItemIOPanelMenu, IItemTypedSlot, ItemStack>>menu(
                    ItemIOPanelMenu::create,
                    () -> SingleTypedSlotScreen::create
            )
            .register();

    public static void setup() {

    }
}
