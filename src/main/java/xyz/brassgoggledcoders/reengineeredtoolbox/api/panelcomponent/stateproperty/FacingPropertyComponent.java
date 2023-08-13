package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
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
    public PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame, @Nullable PanelState current) {
        if (current != null && this.getProperty().getPossibleValues().contains(context.getClickedFace())) {
            return current.setValue(this.getProperty(), context.getClickedFace());
        }
        return null;
    }
}
