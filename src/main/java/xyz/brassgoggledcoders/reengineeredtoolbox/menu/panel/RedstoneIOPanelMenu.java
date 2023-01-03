package xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.IPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredMenus;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.SingleTypedSlotMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSupplier;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneTypedSlot;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import java.util.List;

public class RedstoneIOPanelMenu extends SingleTypedSlotMenu<IRedstoneTypedSlot, RedstoneSupplier> implements IPanelMenu {
    public RedstoneIOPanelMenu(MenuType<?> pMenuType, int pContainerId, Inventory inventory) {
        super(pMenuType, pContainerId, inventory, ContainerLevelAccess.NULL, new RedstoneTypedSlot(), null, null);
        this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(
                () -> this.getTypedSlot().getContent().getAsInt(),
                value -> this.getTypedSlot().setContent(new RedstoneSupplier(
                        () -> value,
                        o -> true,
                        "update"
                ))
        ));
    }

    public RedstoneIOPanelMenu(int pContainerId, Inventory inventory, IRedstoneTypedSlot typedSlot,
                               ContainerLevelAccess access, Direction panelSide, Panel panel) {
        super(ReEngineeredMenus.REDSTONE_IO.get(), pContainerId, inventory, access, typedSlot, panelSide, panel);
        this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(
                () -> this.getTypedSlot().getContent().getAsInt()
        ));
    }

    @Override
    @NotNull
    public List<Port> getPorts() {
        return List.of(
                new Port("redstone", Component.literal("Redstone IO"), -1, TypedSlotTypes.REDSTONE.get())
        );
    }

    public static RedstoneIOPanelMenu create(MenuType<?> menuType, int menuId, Inventory inventory) {
        return new RedstoneIOPanelMenu(menuType, menuId, inventory);
    }
}
