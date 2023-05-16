package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.mixin.DispenserBlockAccessor;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.world.DispenserPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.wrapper.PanelStillValidContainerWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class DispenserPanelEntity extends PanelEntity implements MenuProvider {
    private final Supplier<DispenserBlockEntity> internalDispenser;

    private final FrameSlot[] itemSlots;
    private final FrameSlot redstoneSlot;

    public DispenserPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.internalDispenser = Suppliers.memoize(() -> new DispenserBlockEntity(this.getBlockPos(), this.asDispenser()));
        this.itemSlots = new FrameSlot[]{
                new FrameSlot(ReEngineeredText.ITEM_SLOT_IN),
                new FrameSlot(ReEngineeredText.ITEM_SLOT_IN),
                new FrameSlot(ReEngineeredText.ITEM_SLOT_IN)
        };
        this.redstoneSlot = new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN);
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
        ListTag itemSlotsTag = new ListTag();
        for (FrameSlot itemSlot : this.itemSlots) {
            itemSlotsTag.add(itemSlot.serializeNBT());
        }
        pTag.put("ItemSlots", itemSlotsTag);
        pTag.put("RedstoneSlot", this.redstoneSlot.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.internalDispenser.get().load(pTag.getCompound("Dispenser"));
        ListTag itemSlotsTag = pTag.getList("ItemSlots", Tag.TAG_COMPOUND);
        for (int x = 0; x < this.itemSlots.length; x++) {
            if (itemSlotsTag.size() > x) {
                this.itemSlots[x].deserializeNBT(itemSlotsTag.getCompound(x));
            }
        }
        this.redstoneSlot.deserializeNBT(pTag.getCompound("RedstoneSlot"));
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
