package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;

import java.util.function.BiFunction;

public class IOPanel<T extends ITypedSlot<U>, U> extends Panel {
    private final BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity<T, U>> panelEntityConstructor;

    public IOPanel(BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity<T, U>> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    @NotNull
    public InteractionResult use(IFrameEntity frameEntity, PanelState panelState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (frameEntity.getPanelEntity(pHit.getDirection()) instanceof IOPanelEntity<?, ?> ioPanelEntity) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                frameEntity.openMenu(
                        serverPlayer,
                        ioPanelEntity,
                        new SimpleMenuProvider(
                                ioPanelEntity.getMenuCreator(),
                                this.getName()
                        )
                );
            }
            return InteractionResult.sidedSuccess(frameEntity.getFrameLevel().isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}