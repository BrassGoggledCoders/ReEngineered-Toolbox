package xyz.brassgoggledcoders.reengineeredtoolbox.panel;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.function.BiFunction;

public class TriggeredPanelWithEntity extends PanelWithEntity {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public TriggeredPanelWithEntity(BiFunction<IFrameEntity, PanelState, ? extends PanelEntity> panelEntityConstructor) {
        super(panelEntityConstructor);
        this.registerDefaultState(this.defaultPanelState()
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected void createPanelStateDefinition(StateDefinition.Builder<Panel, PanelState> pBuilder) {
        super.createPanelStateDefinition(pBuilder);
        pBuilder.add(TRIGGERED);
    }
}
