package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.RedstoneTrigger;

public class TankTreadPanelEntity extends PanelEntity {
    private final RedstoneTrigger redstoneTrigger;

    public TankTreadPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);

        this.redstoneTrigger = new RedstoneTrigger(
                this.registerFrameSlot(new FrameSlot(ReEngineeredText.REDSTONE_SLOT_IN, FrameSlotViews.CENTERED_4X4)),
                frameEntity.getCapability(ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER),
                this::powerChanged
        );
    }

    private void powerChanged(boolean powered) {
        if (powered) {
            this.getFrameEntity()
                    .scheduleTick(this.getPanelPosition(), this.getPanel(), 4);
        }
    }

    @Override
    public void scheduledTick() {
        super.scheduledTick();
        BlockPos offset = this.getPanelPosition().offset(this.getFrameEntity());
        Direction facing = this.getPanelPosition().getFacing();

        if (facing != null && !offset.equals(this.getBlockPos())) {
            BlockState treadOn = this.getLevel().getBlockState(offset);
            if (treadOn.isFaceSturdy(this.getLevel(), offset, facing.getOpposite(), SupportType.FULL)) {

            }
        }
    }

    @Override
    public <T> void notifyStorageChanged(Capability<T> frequencyCapability) {
        super.notifyStorageChanged(frequencyCapability);
        this.redstoneTrigger.notifyStorageChanged(frequencyCapability);
    }

    @Override
    public void onFrameSlotChange(FrameSlot frameSlot) {
        super.onFrameSlotChange(frameSlot);
        this.redstoneTrigger.onFrameSlotChange(frameSlot);
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("RedstoneTrigger", this.redstoneTrigger.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.redstoneTrigger.deserializeNBT(pTag.getCompound("RedstoneTrigger"));
    }
}
