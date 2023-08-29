package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelUseOnContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement.IPlacementPanelComponent;

public class FacingPropertyComponent extends PanelStatePropertyComponent<Direction> implements IPlacementPanelComponent {
    public FacingPropertyComponent() {
        this(BlockStateProperties.FACING);
    }

    public FacingPropertyComponent(Property<Direction> property) {
        super(property);
    }

    @Override
    @Nullable
    public PanelState getPanelStateForPlacement(PanelUseOnContext context, IFrameEntity frame, @Nullable PanelState current) {
        IPanelPosition panelPosition = context.getPanelPosition();
        Direction facing = panelPosition.getFacing();
        if (current != null && facing != null && this.getProperty().getPossibleValues().contains(facing)) {
            return current.setValue(this.getProperty(), facing);
        }
        return null;
    }
}
