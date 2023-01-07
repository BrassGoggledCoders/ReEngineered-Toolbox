package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.RedstoneIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSupplier;

import java.util.Optional;

public abstract class RedstoneIOPanelEntity extends IOPanelEntity<IRedstoneTypedSlot, RedstoneSupplier> {
    private int power;

    public RedstoneIOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.power = pTag.getInt("Power");
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.putInt("Power", this.power);
    }

    public int getSignal() {
        return 0;
    }

    @Override
    protected Optional<IRedstoneTypedSlot> getTypedSlot(ITypedSlot<?> typedSlot) {
        if (typedSlot instanceof IRedstoneTypedSlot redstoneTypedSlot) {
            return Optional.of(redstoneTypedSlot);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @NotNull
    protected String getIdentifier() {
        return "redstone";
    }

    @Override
    public TypedSlotType getTypedSlotType() {
        return TypedSlotTypes.REDSTONE.get();
    }

    @Override
    public MenuConstructor getMenuCreator() {
        return (menuId, inventory, player) -> new RedstoneIOPanelMenu(
                menuId,
                inventory,
                this::getSlotForMenu,
                ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()),
                this.getFacing(),
                this.getPanelState().getPanel()
        );
    }
}
