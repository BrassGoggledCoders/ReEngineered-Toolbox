package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.SingleTypedSlotScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.RedstoneIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSupplier;

import javax.annotation.ParametersAreNonnullByDefault;

public class ReEngineeredMenus {

    public static final MenuEntry<RedstoneIOPanelMenu> REDSTONE_IO = ReEngineeredToolbox.getRegistrate()
            .object("redstone_io")
            .menu(RedstoneIOPanelMenu::create, () -> new MenuBuilder.ScreenFactory<RedstoneIOPanelMenu, SingleTypedSlotScreen<RedstoneIOPanelMenu, IRedstoneTypedSlot, RedstoneSupplier>>() {
                @Override
                @NotNull
                @ParametersAreNonnullByDefault
                public SingleTypedSlotScreen<RedstoneIOPanelMenu, IRedstoneTypedSlot, RedstoneSupplier> create(
                        RedstoneIOPanelMenu menu, Inventory inv, Component displayName) {
                    return new SingleTypedSlotScreen<>(menu, inv, displayName);
                }
            })
            .register();

    public static void setup() {

    }
}
