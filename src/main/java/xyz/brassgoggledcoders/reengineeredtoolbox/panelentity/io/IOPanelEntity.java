package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public abstract class IOPanelEntity extends PanelEntity {

    private final FrameSlot ioPort;

    public IOPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                         @NotNull Component identifier) {
        super(frameEntity, panelState);
        this.ioPort = this.registerFrameSlot(new FrameSlot(identifier, FrameSlotViews.CENTERED_4X4));
    }

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
}
