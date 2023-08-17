package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;

public class BasicCapabilityPanelEntity<T, U> extends PanelEntity {
    private final Capability<T> capability;
    private final Capability<U> frequencyCapability;
    private final LazyOptional<T> lazyOptional;
    private final FrameSlot frameSlot;
    private final IOStyle ioStyle;
    private final Function3<FrameSlot, LazyOptional<U>, IOStyle, T> lazyOptionalCreator;

    public BasicCapabilityPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState,
                                      Capability<T> capability, Capability<U> frequencyCapability, FrameSlot frameSlot,
                                      IOStyle ioStyle, Function3<FrameSlot, LazyOptional<U>, IOStyle, T> lazyOptionalCreator) {
        super(frameEntity, panelState);
        this.capability = capability;
        this.frequencyCapability = frequencyCapability;
        this.frameSlot = this.registerFrameSlot(frameSlot);
        this.ioStyle = ioStyle;
        this.lazyOptionalCreator = lazyOptionalCreator;
        this.lazyOptional = LazyOptional.of(this::createHandler);
    }

    private T createHandler() {
        return this.lazyOptionalCreator.apply(
                this.frameSlot,
                this.getFrameEntity().getCapability(frequencyCapability),
                this.ioStyle
        );
    }

    @Override
    @NotNull
    public <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        if (cap == this.capability) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.frameSlot.deserializeNBT(pTag.getCompound("FrameSlot"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.put("FrameSlot", this.frameSlot.serializeNBT());
    }
}
