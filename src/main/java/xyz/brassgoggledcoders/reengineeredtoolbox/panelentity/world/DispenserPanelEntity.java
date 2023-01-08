package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;

import java.util.function.Supplier;

public class DispenserPanelEntity extends PanelEntity {
    private final Supplier<DispenserBlockEntity> internalDispenser;

    private int connectedSlotId;

    public DispenserPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.internalDispenser = Suppliers.memoize(() -> {
            DispenserBlockEntity blockEntity = new DispenserBlockEntity(this.getBlockPos(), this.asDispenser());
            blockEntity.setLevel(this.getLevel());
            return blockEntity;
        });
    }

    @Override
    public void setPortConnection(Port port, int slotNumber) {
        if (port.identifier().equals("dispenser")) {
            ITypedSlotHolder typedSlotHolder = this.getFrameEntity()
                    .getTypedSlotHolder();

            if (typedSlotHolder.getSlot(slotNumber) instanceof IItemTypedSlot) {
                this.connectedSlotId = slotNumber;
            }
        }
    }

    public BlockState asDispenser() {
        return Blocks.DISPENSER.defaultBlockState()
                .setValue(DispenserBlock.FACING, this.getFacing());
    }

    public DispenserBlockEntity getDispenserEntity() {
        return internalDispenser.get();
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
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.getDispenserEntity().load(pTag.getCompound("Dispenser"));
    }
}
