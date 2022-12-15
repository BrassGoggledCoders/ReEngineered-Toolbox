package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PanelStateProperties {
    public static final EnumProperty<Direction> FACING_DIRECTION = EnumProperty.create("facing", Direction.class);
}
