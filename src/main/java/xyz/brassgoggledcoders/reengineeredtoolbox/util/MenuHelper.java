package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.BlockPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

public class MenuHelper {
    public static boolean checkPanelMenuValid(ContainerLevelAccess levelAccess, Player player, Direction direction, Panel panel) {
        return levelAccess.evaluate((level, blockPos) -> {
            double distance = player.distanceToSqr(
                    (double) blockPos.getX() + 0.5D,
                    (double) blockPos.getY() + 0.5D,
                    (double) blockPos.getZ() + 0.5D
            );

            boolean matchesPanel = false;

            if (level.getBlockEntity(blockPos) instanceof IFrameEntity frame) {
                matchesPanel = frame.getPanelState(BlockPanelPosition.fromDirection(direction)).is(panel);
            }

            return distance <= 64 && matchesPanel;
        }).orElse(true);
    }
}
