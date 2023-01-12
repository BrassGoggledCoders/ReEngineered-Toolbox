package xyz.brassgoggledcoders.reengineeredtoolbox.panel;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.function.BiFunction;

public class PanelWithMenu<T extends PanelEntity & MenuProvider> extends Panel {
    private final BiFunction<IFrameEntity, PanelState, T> panelEntityConstructor;

    public PanelWithMenu(BiFunction<IFrameEntity, PanelState, T> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    @NotNull
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

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}
