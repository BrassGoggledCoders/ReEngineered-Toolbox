package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.ListeningConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.MovingConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.mixin.DispenserBlockAccessor;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.world.DispenserPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.wrapper.PanelStillValidContainerWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

public class DispenserPanelEntity extends PanelEntity implements MenuProvider {
    private final Supplier<DispenserBlockEntity> internalDispenser;

    private final Port itemPort;
    private final Port redstonePort;
    private final MovingConnection<IItemTypedSlot, ItemStack, IItemHandler> itemConnection;
    private final ListeningConnection<IRedstoneTypedSlot, Integer> redstoneConnection;

    public DispenserPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.internalDispenser = Suppliers.memoize(() -> new DispenserBlockEntity(this.getBlockPos(), this.asDispenser()));
        this.itemPort = new Port(
                "itemIn",
                panelState.getPanel().getName(),
                TypedSlotTypes.ITEM.get()
        );
        this.redstonePort = new Port(
                "redstoneIn",
                panelState.getPanel().getName(),
                TypedSlotTypes.REDSTONE.get()
        );
        this.itemConnection = MovingConnection.itemConnection(
                frameEntity.getTypedSlotHolder(),
                itemPort,
                () -> this.internalDispenser.get()
                        .getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElseThrow(() -> new IllegalStateException("Stored Dispenser has No Inventory")),
                MovingConnection.ConnectionDirection.FROM_SLOT
        );
        this.redstoneConnection = ListeningConnection.redstoneConsumer(
                frameEntity.getTypedSlotHolder(),
                redstonePort,
                this::setPowerAndUpdate
        );
    }

    @Override
    public void serverTick() {
        super.serverTick();
        this.itemConnection.tick();
    }

    @Override
    public void setPortConnection(Port port, int slotNumber) {
        this.itemConnection.setSlotConnector(port, slotNumber);
        this.redstoneConnection.setSlotConnector(port, slotNumber);
    }

    private void setPowerAndUpdate(int power) {
        if (power > 0 != this.getPanelState().getValue(BlockStateProperties.TRIGGERED)) {
            if (power > 0) {
                this.getFrameEntity()
                        .scheduleTick(this.getFacing(), this.getPanel(), 4);
            }
            this.getFrameEntity()
                    .putPanelState(this.getFacing(), this.getPanelState().setValue(BlockStateProperties.TRIGGERED, power > 0), true);
        }
    }

    @Override
    public void scheduledTick() {
        super.scheduledTick();
        if (Blocks.DISPENSER instanceof DispenserBlockAccessor blockAccessor && this.getLevel() instanceof ServerLevel serverLevel) {
            DispenserBlockEntity dispenserBlockEntity = this.getDispenserEntity();
            int i = dispenserBlockEntity.getRandomSlot(this.getLevel().random);
            if (i < 0) {
                this.getLevel().levelEvent(1001, this.getBlockPos(), 0);
                this.getLevel().gameEvent(null, GameEvent.DISPENSE_FAIL, this.getBlockPos());
            } else {
                ItemStack itemStack = dispenserBlockEntity.getItem(i);
                DispenseItemBehavior dispenserBehavior = blockAccessor.callGetDispenseMethod(itemStack);
                if (dispenserBehavior != DispenseItemBehavior.NOOP) {
                    dispenserBlockEntity.setItem(i, dispenserBehavior.dispense(
                            new PanelEntityBlockSource(this, serverLevel),
                            itemStack
                    ));
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setPanelState(@NotNull PanelState panelState) {
        super.setPanelState(panelState);
        this.getDispenserEntity()
                .setBlockState(this.asDispenser());
    }

    @Override
    public Map<Port, Integer> getPorts() {
        return Map.of(
                this.itemPort, this.itemConnection.getSlotId(),
                this.redstonePort, this.redstoneConnection.getSlotId()
        );
    }

    public BlockState asDispenser() {
        return Blocks.DISPENSER.defaultBlockState()
                .setValue(DispenserBlock.FACING, this.getFacing())
                .setValue(DispenserBlock.TRIGGERED, this.getPanelState().getValue(DispenserPanel.TRIGGERED));
    }

    public DispenserBlockEntity getDispenserEntity() {
        DispenserBlockEntity dispenserPanelEntity = internalDispenser.get();
        if (dispenserPanelEntity.getLevel() == null) {
            dispenserPanelEntity.setLevel(this.getLevel());
        }
        return dispenserPanelEntity;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> lazyOptional = this.getDispenserEntity().getCapability(cap, side);
        if (lazyOptional.isPresent()) {
            return lazyOptional;
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("Dispenser", this.getDispenserEntity().saveWithoutMetadata());
        pTag.put("ItemConnection", this.itemConnection.serializeNBT());
        pTag.put("RedstoneConnection", this.redstoneConnection.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.internalDispenser.get().load(pTag.getCompound("Dispenser"));
        this.itemConnection.deserializeNBT(pTag.getCompound("ItemConnection"));
        this.redstoneConnection.deserializeNBT(pTag.getCompound("RedstoneConnection"));
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getPanelState().getPanel().getName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DispenserMenu(
                pContainerId,
                pPlayerInventory,
                new PanelStillValidContainerWrapper(
                        this.getDispenserEntity(),
                        this::stillValid
                )
        );
    }
}
