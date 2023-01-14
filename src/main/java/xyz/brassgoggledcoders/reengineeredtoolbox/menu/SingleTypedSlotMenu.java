package xyz.brassgoggledcoders.reengineeredtoolbox.menu;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.slot.TypedMenuSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.MenuHelper;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;

import java.util.function.Supplier;

public class SingleTypedSlotMenu<T extends ITypedSlot<U>, U> extends AbstractContainerMenu implements IPropertyManaged {
    private final PropertyManager propertyManager;
    private final Supplier<T> typedSlot;
    private final Direction panelSide;
    private final Panel panel;
    private final ContainerLevelAccess access;
    private final Inventory inventory;

    public SingleTypedSlotMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, ContainerLevelAccess access,
                               Supplier<T> typedSlot, Direction panelSide, Panel panel) {
        super(pMenuType, pContainerId);
        this.propertyManager = ReEngineeredToolbox.getSyncing().createManager(pContainerId);
        this.access = access;
        this.typedSlot = typedSlot;
        this.panelSide = panelSide;
        this.panel = panel;
        this.inventory = inventory;

        this.addSlot(new TypedMenuSlot<>(typedSlot::get, 0, 80, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (typedSlot.get() instanceof IItemTypedSlot) {
            Slot slot = this.slots.get(pIndex);
            if (slot.hasItem()) {
                ItemStack itemstack1 = slot.getItem();
                itemstack = itemstack1.copy();
                if (pIndex < 1) {
                    if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return !TypedSlotTypes.BLANK.is(this.typedSlot.get().getType()) && !ReEngineeredPanels.PLUG.is(this.panel) &&
                MenuHelper.checkPanelMenuValid(this.access, pPlayer, this.panelSide, this.panel);
    }

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();
        this.propertyManager.sendChanges(inventory, true);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.propertyManager.sendChanges(inventory, false);
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    public T getTypedSlot() {
        return typedSlot.get();
    }
}
