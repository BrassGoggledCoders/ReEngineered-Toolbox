package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;

public abstract class RedstoneIOPanelEntity extends IOPanelEntity {
    private int power;

    public RedstoneIOPanelEntity( @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                                 @NotNull Component identifier) {
        super(frameEntity, panelState, identifier);
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
}
