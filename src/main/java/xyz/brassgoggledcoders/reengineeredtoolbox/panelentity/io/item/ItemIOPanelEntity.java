package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item;

import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.ItemIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.ItemTypedSlot;

import java.util.Optional;

public class ItemIOPanelEntity extends IOPanelEntity<IItemTypedSlot, ItemStack> {

    private final IItemTypedSlot panelTypedSlot;
    private final LazyOptional<IItemHandler> lazyOptional;

    public ItemIOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.panelTypedSlot = new ItemTypedSlot();
        this.lazyOptional = LazyOptional.of(this::getSlotForMenu);
    }

    @Override
    protected Optional<IItemTypedSlot> getTypedSlot(ITypedSlot<?> typedSlot) {
        if (typedSlot instanceof IItemTypedSlot itemTypedSlot) {
            return Optional.of(itemTypedSlot);
        }

        return Optional.empty();
    }

    @Override
    @NotNull
    protected String getIdentifier() {
        return "item";
    }

    @Override
    public IItemTypedSlot getSlotForMenu() {
        return this.panelTypedSlot;
    }

    @Override
    public MenuConstructor getMenuCreator() {
        return (menuId, inventory, player) -> new ItemIOPanelMenu(
                menuId,
                inventory,
                this::getSlotForMenu,
                ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()),
                this.getFacing(),
                this.getPanelState().getPanel()
        );
    }

    @Override
    public TypedSlotType getTypedSlotType() {
        return TypedSlotTypes.ITEM.get();
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.lazyOptional.invalidate();
    }
}
