package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FacingPropertyComponent extends PanelStatePropertyComponent<Direction> {
    public FacingPropertyComponent() {
        super(BlockStateProperties.FACING);
    }
}
