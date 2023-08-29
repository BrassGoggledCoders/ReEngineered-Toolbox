package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public class TankTreadPanelEntity extends PanelEntity {
    public static IntegerProperty TREAD_DIRECTION = IntegerProperty.create("tread", 0, 3);

    public TankTreadPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);
    }
}
