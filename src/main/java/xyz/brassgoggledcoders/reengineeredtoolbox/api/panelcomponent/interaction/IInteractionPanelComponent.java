package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public interface IInteractionPanelComponent  {
    @NotNull
    InteractionResult use(IFrameEntity frameEntity, PanelState panelState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit);
}
