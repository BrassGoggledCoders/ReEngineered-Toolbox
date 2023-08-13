package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public class MenuInteractionPanelComponent extends InteractionPanelComponent {
    @Override
    public InteractionResult use(IFrameEntity frameEntity, PanelState panelState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isSecondaryUseActive()) {
            PanelEntity panelEntity = frameEntity.getPanelEntity(pHit.getDirection());
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
