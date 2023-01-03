package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.RedstoneIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.SingleTypedSlotScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSupplier;

public class ReEngineeredMenus {

    @SuppressWarnings("RedundantTypeArguments")
    public static final MenuEntry<RedstoneIOPanelMenu> REDSTONE_IO = ReEngineeredToolbox.getRegistrate()
            .object("redstone_io")
            .<RedstoneIOPanelMenu, SingleTypedSlotScreen<RedstoneIOPanelMenu, IRedstoneTypedSlot, RedstoneSupplier>>menu(
                    RedstoneIOPanelMenu::create,
                    () -> SingleTypedSlotScreen::create
            )
            .register();

    public static void setup() {

    }
}
