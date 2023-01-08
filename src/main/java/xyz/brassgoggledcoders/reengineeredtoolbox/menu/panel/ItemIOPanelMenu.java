package xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.IPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredMenus;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.SingleTypedSlotMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.ItemTypedSlot;

import java.util.List;
import java.util.function.Supplier;

public class ItemIOPanelMenu extends SingleTypedSlotMenu<IItemTypedSlot, ItemStack> implements IPanelMenu {
    public ItemIOPanelMenu(MenuType<?> pMenuType, int pContainerId, Inventory inventory) {
        super(pMenuType, pContainerId, inventory, ContainerLevelAccess.NULL, Suppliers.memoize(ItemTypedSlot::new), null, null);
    }

    public ItemIOPanelMenu(int pContainerId, Inventory inventory, Supplier<IItemTypedSlot> typedSlot,
                           ContainerLevelAccess access, Direction panelSide, Panel panel) {
        super(ReEngineeredMenus.ITEM_IO.get(), pContainerId, inventory, access, typedSlot, panelSide, panel);
    }

    @Override
    @NotNull
    public List<Port> getPorts() {
        return List.of(
                new Port("itemstack", Component.literal("ItemStack IO"), TypedSlotTypes.ITEM.get())
        );
    }

    public static ItemIOPanelMenu create(MenuType<?> menuType, int menuId, Inventory inventory) {
        return new ItemIOPanelMenu(menuType, menuId, inventory);
    }
}
