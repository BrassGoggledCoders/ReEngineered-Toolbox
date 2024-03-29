package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.mixin.DispenserBlockAccessor;

import java.util.function.Supplier;

public class DispenserPanelEntity extends PanelEntity {
    private final Supplier<DispenserBlockEntity> internalDispenser;

    private final FrameSlot itemSlot;
    private final FrameSlot redstoneSlot;
    private final LazyOptional<IFrequencyItemHandler> itemHandlerLazyOptional;
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandlerLazyOptional;

    public DispenserPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);
        this.internalDispenser = Suppliers.memoize(() -> new DispenserBlockEntity(this.getBlockPos(), this.asDispenser()));
        this.itemSlot = this.registerFrameSlot(new FrameSlot(ReEngineeredText.ITEM_SLOT_IN, FrameSlotViews.LEFT_4X4));
        this.redstoneSlot = this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN, FrameSlotViews.RIGHT_4X4));
        this.itemHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_ITEM_HANDLER);
        this.redstoneHandlerLazyOptional = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
    }

    private void setPowerAndUpdate(int power) {
        if (power > 0 != this.getPanelState().getValue(BlockStateProperties.TRIGGERED)) {
            if (power > 0) {
                this.getFrameEntity()
                        .scheduleTick(this.getPanelPosition(), this.getPanel(), 4);
            }
            this.getFrameEntity()
                    .putPanelState(
                            this.getPanelPosition(),
                            this.getPanelState()
                                    .setValue(BlockStateProperties.TRIGGERED, power > 0), true
                    );
        }
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.redstoneHandlerLazyOptional.map(redstoneHandler -> redstoneHandler.getPower(redstoneSlot.getFrequency()))
                    .ifPresent(this::setPowerAndUpdate);
        }
    }

    @Override
    public void scheduledTick() {
        super.scheduledTick();
        if (Blocks.DISPENSER instanceof DispenserBlockAccessor blockAccessor && this.getLevel() instanceof ServerLevel serverLevel) {
            ItemStack itemStack = this.itemHandlerLazyOptional.map(itemHandler -> itemHandler.getStackInSlot(this.itemSlot.getFrequency()))
                    .orElse(ItemStack.EMPTY);
            if (itemStack.isEmpty()) {
                this.getLevel().levelEvent(1001, this.getBlockPos(), 0);
                this.getLevel().gameEvent(null, GameEvent.DISPENSE_FAIL, this.getBlockPos());
            } else {
                DispenseItemBehavior dispenserBehavior = blockAccessor.callGetDispenseMethod(itemStack);
                if (dispenserBehavior != DispenseItemBehavior.NOOP) {
                    this.itemHandlerLazyOptional.ifPresent(itemHandler -> itemHandler.setStackInSlot(
                            this.itemSlot.getFrequency(),
                            dispenserBehavior.dispense(
                                    new PanelEntityBlockSource(this, serverLevel),
                                    itemStack
                            )
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
    @SuppressWarnings("deprecation")
    public void setPanelPosition(@NotNull IPanelPosition panelPosition) {
        super.setPanelPosition(panelPosition);
        this.internalDispenser.get()
                .setBlockState(this.asDispenser());
    }

    public BlockState asDispenser() {
        Direction facing = this.getPanelPosition().getFacing();
        BlockState blockState = Blocks.DISPENSER.defaultBlockState()
                .setValue(DispenserBlock.TRIGGERED, this.getPanelState().getValue(BlockStateProperties.TRIGGERED));
        if (facing != null) {
            blockState = blockState.setValue(DispenserBlock.FACING, facing);
        }
        return blockState;
    }

    public DispenserBlockEntity getDispenserEntity() {
        DispenserBlockEntity dispenserPanelEntity = internalDispenser.get();
        if (dispenserPanelEntity.getLevel() == null) {
            dispenserPanelEntity.setLevel(this.getLevel());
        }
        return dispenserPanelEntity;
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("Dispenser", this.getDispenserEntity().saveWithoutMetadata());
        pTag.put("ItemSlot", this.itemSlot.serializeNBT());
        pTag.put("RedstoneSlot", this.redstoneSlot.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.internalDispenser.get().load(pTag.getCompound("Dispenser"));
        this.itemSlot.deserializeNBT(pTag.getCompound("ItemSlot"));
        this.redstoneSlot.deserializeNBT(pTag.getCompound("RedstoneSlot"));
    }
}
