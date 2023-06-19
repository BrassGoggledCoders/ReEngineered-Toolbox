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

import java.util.function.BiFunction;

public class IOPanel extends Panel {
    private final BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity> panelEntityConstructor;

    public IOPanel(BiFunction<IFrameEntity, PanelState, ? extends IOPanelEntity> panelEntityConstructor) {
        this.panelEntityConstructor = panelEntityConstructor;
    }

    @Override
    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return panelEntityConstructor.apply(frame, panelState);
    }
}
