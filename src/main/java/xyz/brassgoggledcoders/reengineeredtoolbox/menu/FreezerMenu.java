package xyz.brassgoggledcoders.reengineeredtoolbox.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.TankView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.Property;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import java.util.function.Supplier;

public class FreezerMenu extends AbstractContainerMenu implements IPropertyManaged {
    private final ContainerLevelAccess levelAccess;
    private final PropertyManager propertyManager;
    private final Inventory inventory;
    private final Property<TankView> tankView;
    private final Property<ProgressView> progressView;

    public FreezerMenu(MenuType<FreezerMenu> type, int windowId, Inventory inventory) {
        super(type, windowId);
        this.levelAccess = ContainerLevelAccess.NULL;
        this.inventory = inventory;
        this.propertyManager = ReEngineeredToolbox.getSyncing()
                .createManager(windowId);

        this.tankView = this.propertyManager.addTrackedProperty(PropertyTypes.TANK_VIEW.create());
        this.progressView = this.propertyManager.addTrackedProperty(PropertyTypes.PROGRESS_VIEW.create());

        addSlots(new ItemStackHandler(2), inventory);
    }

    public FreezerMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, ContainerLevelAccess levelAccess,
                       IItemHandlerModifiable itemHandler, Supplier<TankView> tankViewSupplier, Supplier<ProgressView> progressSupplier) {
        super(pMenuType, pContainerId);
        this.levelAccess = levelAccess;
        this.inventory = inventory;
        this.propertyManager = ReEngineeredToolbox.getSyncing()
                .createManager(pContainerId);

        this.tankView = this.propertyManager.addTrackedProperty(PropertyTypes.TANK_VIEW.create(
                tankViewSupplier
        ));
        this.progressView = this.propertyManager.addTrackedProperty(PropertyTypes.PROGRESS_VIEW.create(
                progressSupplier
        ));

        addSlots(itemHandler, inventory);
    }

    private void addSlots(IItemHandler itemHandler, Inventory inventory) {
        this.addSlot(new SlotItemHandler(itemHandler, 0, 71, 35));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 125, 35));

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
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    public TankView getTankView() {
        return this.tankView.getOrElse(TankView.NULL);
    }

    public ProgressView getProgress() {
        return this.progressView.getOrElse(ProgressView.NULL);
    }


    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (inventory.player instanceof ServerPlayer serverPlayer) {
            this.propertyManager.sendChanges(serverPlayer, false);
        }
    }

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();
        if (inventory.player instanceof ServerPlayer serverPlayer) {
            this.propertyManager.sendChanges(serverPlayer, true);
        }
    }
}
