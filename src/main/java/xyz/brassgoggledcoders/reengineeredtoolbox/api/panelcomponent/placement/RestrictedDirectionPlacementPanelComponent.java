package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelUseOnContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public class RestrictedDirectionPlacementPanelComponent extends PanelComponent implements IPlacementPanelComponent, IPlacementRequirementPanelComponent {
    private final Direction[] validDirections;

    public RestrictedDirectionPlacementPanelComponent(Direction... validDirections) {
        this.validDirections = validDirections;
    }

    @Override
    @Nullable
    public PanelState getPanelStateForPlacement(PanelUseOnContext context, IFrameEntity frame, PanelState current) {
        for (Direction direction : validDirections) {
            if (direction == context.hitResult().panelPosition().getFacing()) {
                return current;
            }
        }
        return null;
    }

    @Override
    public Direction[] getValidDirections() {
        return this.validDirections;
    }
}
