package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.ListeningConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.DaylightDetectorPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneIOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;

import java.util.function.BiFunction;

public class DaylightDetectorPanel extends IOPanel<IRedstoneTypedSlot, Integer, ListeningConnection<IRedstoneTypedSlot, Integer>> {
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
