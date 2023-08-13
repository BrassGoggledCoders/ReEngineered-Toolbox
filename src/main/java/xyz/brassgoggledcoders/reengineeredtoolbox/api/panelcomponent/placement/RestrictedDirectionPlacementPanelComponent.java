package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

import java.util.Arrays;
import java.util.Collection;

public class RestrictedDirectionPlacementPanelComponent extends PanelComponent implements IPlacementPanelComponent {
    private final Collection<Direction> validDirections;

    public RestrictedDirectionPlacementPanelComponent(Direction... validDirections) {
        this(Arrays.asList(validDirections));
    }
    public RestrictedDirectionPlacementPanelComponent(Collection<Direction> validDirections) {
        this.validDirections = validDirections;
    }

    @Override
    @Nullable
    public PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame, PanelState current) {
        if (validDirections.contains(context.getClickedFace())) {
            return current;
        }
        return null;
    }
}
