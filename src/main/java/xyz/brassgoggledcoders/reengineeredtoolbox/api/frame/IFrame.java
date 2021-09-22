package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.UUID;

public interface IFrame extends ICapabilityProvider {
    PanelInfo getInfoBySide(Direction direction);

    PanelInfo getInfoByUniqueId(UUID uniqueId);

    boolean placePanel(IPanelPlacement panelPlacement);

    BlockPos getFramePos();

    IWorld getFrameLevel();
}
