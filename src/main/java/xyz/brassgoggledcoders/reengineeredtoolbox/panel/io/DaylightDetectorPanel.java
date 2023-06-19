package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.DaylightDetectorPanelEntity;

public class DaylightDetectorPanel extends IOPanel {
    public DaylightDetectorPanel() {
        super(DaylightDetectorPanelEntity::new);
    }

    @Override
    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState) {
        return true;
    }

    @Nullable
    public Property<Direction> getFacingProperty() {
        return null;
    }

    @Override
    public PanelState getPanelStateForPlacement(UseOnContext context, IFrameEntity frame) {
        if (context.getClickedFace() == Direction.UP) {
            return this.defaultPanelState();
        } else {
            return null;
        }
    }
}
