package xyz.brassgoggledcoders.reengineeredtoolbox.panelcomponent.panelentity;

import com.mojang.datafixers.util.Function3;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.panelentity.IPanelEntityPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.BasicCapabilityPanelEntity;

public class BasicCapabilityPanelEntityPanelComponent<T, U> extends PanelComponent implements IPanelEntityPanelComponent {
    private final Capability<T> capability;
    private final Capability<U> frequencyCapability;
    private final FrameSlot frameSlot;
    private final IOStyle ioStyle;
    private final Function3<FrameSlot, LazyOptional<U>, IOStyle, T> lazyOptionalCreator;

    public BasicCapabilityPanelEntityPanelComponent(Capability<T> capability, Capability<U> frequencyCapability,
                                                    Component title, IOStyle ioStyle,
                                                    Function3<FrameSlot, LazyOptional<U>, IOStyle, T> lazyOptionalCreator) {
        this.capability = capability;
        this.frequencyCapability = frequencyCapability;
        this.frameSlot = new FrameSlot(title, FrameSlotViews.CENTERED_4X4);
        this.ioStyle = ioStyle;
        this.lazyOptionalCreator = lazyOptionalCreator;
    }


    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return new BasicCapabilityPanelEntity<>(
                frame,
                panelState,
                this.capability,
                this.frequencyCapability,
                this.frameSlot,
                this.ioStyle,
                this.lazyOptionalCreator
        );
    }
}
