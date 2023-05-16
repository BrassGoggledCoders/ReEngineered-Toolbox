package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneIOPanelEntity;


import java.util.function.BiFunction;

public class RedstoneIOPanel extends IOPanel {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public RedstoneIOPanel(BiFunction<IFrameEntity, PanelState, ? extends RedstoneIOPanelEntity> panelEntityConstructor) {
        super(panelEntityConstructor);
        this.registerDefaultState(this.defaultPanelState()
                .setValue(POWERED, false)
        );
    }

    @Override
    protected void createPanelStateDefinition(StateDefinition.Builder<Panel, PanelState> pBuilder) {
        super.createPanelStateDefinition(pBuilder);
        pBuilder.add(POWERED);
    }

    @Override
    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState) {
        return true;
    }

    @Override
    public int getSignal(IFrameEntity frameEntity, PanelState panelState) {
        if (frameEntity.getPanelEntity(panelState.getFacing()) instanceof RedstoneIOPanelEntity redstoneIOPanelEntity) {
            return redstoneIOPanelEntity.getSignal();
        } else {
            return 0;
        }
    }
}
