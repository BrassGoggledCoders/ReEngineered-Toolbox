package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotView;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;

import java.util.Collections;
import java.util.List;

public abstract class IOPanelEntity extends PanelEntity {

    private final FrameSlot ioPort;
    private final List<FrameSlot> frameSlots;

    public IOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.ioPort = new FrameSlot(this.getIdentifier(), FrameSlotViews.CENTERED_4X4);
        this.frameSlots = Collections.singletonList(this.ioPort);
    }

    @NotNull
    protected abstract Component getIdentifier();

    public FrameSlot getIoPort() {
        return ioPort;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.ioPort.deserializeNBT(pTag.getCompound("IOPort"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("IOPort", this.ioPort.serializeNBT());
    }

    @Override
    public List<FrameSlot> getFrameSlots() {
        return this.frameSlots;
    }
}
