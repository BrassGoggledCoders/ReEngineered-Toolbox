package xyz.brassgoggledcoders.reengineeredtoolbox.panel.world;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser.DispenserPanelEntity;

public class DispenserPanel extends Panel {

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public DispenserPanel() {
        this.registerDefaultState(this.defaultPanelState()
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected void createPanelStateDefinition(StateDefinition.Builder<Panel, PanelState> pBuilder) {
        super.createPanelStateDefinition(pBuilder);
        pBuilder.add(TRIGGERED);
    }

    @Override
    @NotNull
    public InteractionResult use(IFrameEntity frameEntity, PanelState panelState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isSecondaryUseActive() && frameEntity.getPanelEntity(pHit.getDirection()) instanceof DispenserPanelEntity dispenserPanelEntity) {
            frameEntity.openMenu(
                    pPlayer,
                    dispenserPanelEntity,
                    dispenserPanelEntity
            );
            return InteractionResult.sidedSuccess(true);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return ReEngineeredPanels.DISPENSER.createPanelEntity(frame, panelState);
    }
}
