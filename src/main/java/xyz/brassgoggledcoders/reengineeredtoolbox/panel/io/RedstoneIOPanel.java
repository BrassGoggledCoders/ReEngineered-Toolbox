package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.BiFunction;

public class RedstoneIOPanel extends Panel {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor;

    public RedstoneIOPanel(BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor) {
        this.registerDefaultState(this.defaultPanelState()
                .setValue(POWERED, false)
        );
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    protected void createPanelStateDefinition(StateDefinition.Builder<Panel, PanelState> pBuilder) {
        super.createPanelStateDefinition(pBuilder);
        pBuilder.add(POWERED);
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }

    @Override
    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState) {
        return true;
    }
}
