package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.redstone;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

import java.util.function.ObjIntConsumer;

public class RedstoneNorLatchPanelEntity extends PanelEntity {
    private final FrameSlot[] frameSlots;
    private final LazyOptional<IFrequencyRedstoneHandler> redstoneHandler;

    public RedstoneNorLatchPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);
        this.frameSlots = new FrameSlot[] {
                this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN_1, FrameSlotViews.TOP_LEFT_4X4)),
                this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_OUT_1, FrameSlotViews.BOTTOM_LEFT_4X4)),
                this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN_2, FrameSlotViews.TOP_RIGHT_4X4)),
                this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_OUT_2, FrameSlotViews.BOTTOM_RIGHT_4X4))
        };
        this.redstoneHandler = frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER);
        this.redstoneHandler.ifPresent(value -> value.addPowerProvider(
                this,
                RedstoneNorLatchPanelEntity::providePower
        ));
    }

    public void providePower(ObjIntConsumer<Frequency> powerSubmit) {
        if (this.getPanelState().getValue(BlockStateProperties.POWERED)) {
            powerSubmit.accept(this.frameSlots[1].getFrequency(), 15);
        } else {
            powerSubmit.accept(this.frameSlots[3].getFrequency(), 15);
        }

    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        ListTag frameSlotListTag = new ListTag();
        for (int i = 0; i < frameSlots.length; i++) {
            CompoundTag compoundTag = frameSlots[i].serializeNBT();
            compoundTag.putInt("SlotId", i);
            frameSlotListTag.add(compoundTag);
        }
        pTag.put("FrameSlots", frameSlotListTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ListTag frameSlotListTag = pTag.getList("FrameSlots", Tag.TAG_COMPOUND);
        for (int i = 0; i < frameSlotListTag.size(); i++) {
            CompoundTag compoundTag = frameSlotListTag.getCompound(i);
            int slotId = compoundTag.getInt("SlotId");
            frameSlots[slotId].deserializeNBT(compoundTag);
        }
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        super.notifyStorageChanged(frequencyCapability);
        if (frequencyCapability == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
            this.redstoneHandler.ifPresent(this::setPowerAndUpdate);
        }
    }

    public void setPowerAndUpdate(IFrequencyRedstoneHandler redstoneHandler) {
        FrameSlot toCheck = this.frameSlots[0];
        if (this.getPanelState().getValue(BlockStateProperties.POWERED)) {
            toCheck = this.frameSlots[2];
        }

        if (redstoneHandler.getPower(toCheck.getFrequency()) > 0) {
            this.getFrameEntity().updatePanelState(
                    this.getPanelPosition(),
                    panelState -> panelState.cycle(BlockStateProperties.POWERED)
            );
        }
    }

}
