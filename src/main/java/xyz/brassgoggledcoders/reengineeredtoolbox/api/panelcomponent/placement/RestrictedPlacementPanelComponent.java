package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import java.util.Arrays;
import java.util.Collection;

public class RestrictedPlacementPanelComponent extends PlacementPanelComponent {
    private final Collection<Direction> validDirections;

    public RestrictedPlacementPanelComponent(Direction... validDirections) {
        this(Arrays.asList(validDirections));
    }
    public RestrictedPlacementPanelComponent(Collection<Direction> validDirections) {
        this.validDirections = validDirections;
    }

    @Override
    @Nullable
    public PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame) {
        if (validDirections.contains(context.getClickedFace())) {
            return super.getPanelStateForPlacement(context, frame);
        }
        return null;
    }
}
