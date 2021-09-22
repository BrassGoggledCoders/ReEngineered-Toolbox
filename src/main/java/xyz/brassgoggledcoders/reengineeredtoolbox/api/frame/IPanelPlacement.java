package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import javax.annotation.Nullable;

public interface IPanelPlacement {
    Direction getPlacementFace();

    PanelState getPanelState();

    @Nullable
    CompoundNBT getPanelEntityNBT();
}
