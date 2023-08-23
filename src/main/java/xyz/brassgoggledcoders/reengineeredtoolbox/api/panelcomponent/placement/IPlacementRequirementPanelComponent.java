package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.core.Direction;

public interface IPlacementRequirementPanelComponent {

    Direction[] getValidDirections();

    default boolean isValidDirection(Direction direction) {
        for (Direction validDirection : this.getValidDirections()) {
            if (validDirection == direction) {
                return true;
            }
        }
        return false;
    }
}
