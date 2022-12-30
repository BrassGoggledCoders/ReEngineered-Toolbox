package xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.IPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelConnectionInfo;
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
    @Nullable
    public PanelConnectionInfo getConnectionInfo() {
        return new PanelConnectionInfo(
                (short) this.containerId,
                List.of(
                        new PanelConnectionInfo.Connection("redstone", TypedSlotTypes.REDSTONE.get()),
                        new PanelConnectionInfo.Connection("redstone1", TypedSlotTypes.REDSTONE.get()),
                        new PanelConnectionInfo.Connection("redstone2", TypedSlotTypes.REDSTONE.get()),
                        new PanelConnectionInfo.Connection("redstone3", TypedSlotTypes.REDSTONE.get()),
                        new PanelConnectionInfo.Connection("redstone4", TypedSlotTypes.REDSTONE.get()),
                        new PanelConnectionInfo.Connection("redstone5", TypedSlotTypes.REDSTONE.get())
                )
        );
    }

    public static RedstoneIOPanelMenu create(MenuType<?> menuType, int menuId, Inventory inventory) {
        return new RedstoneIOPanelMenu(menuType, menuId, inventory);
    }
}
