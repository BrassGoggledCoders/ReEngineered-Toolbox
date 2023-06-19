package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;

import java.util.List;

public interface IPanelMenu {

    @NotNull
    List<FrameSlot> getFrameSlots();
}
