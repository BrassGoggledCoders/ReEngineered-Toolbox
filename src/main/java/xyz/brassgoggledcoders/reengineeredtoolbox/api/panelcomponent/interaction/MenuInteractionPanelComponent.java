package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.BlockPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public class MenuInteractionPanelComponent extends PanelComponent implements IInteractionPanelComponent {
    @Override
    @NotNull
    public InteractionResult use(PanelState panelState, IFrameEntity frameEntity, IPanelPosition panelPosition, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isSecondaryUseActive()) {
            PanelEntity panelEntity = frameEntity.getPanelEntity(BlockPanelPosition.fromDirection(pHit.getDirection()));
            if (panelEntity instanceof MenuProvider menuProvider) {
                frameEntity.openMenu(
                        pPlayer,
                        panelEntity,
                        menuProvider
                );
            }

            return InteractionResult.sidedSuccess(true);
        } else {
            return InteractionResult.PASS;
        }
    }
}
