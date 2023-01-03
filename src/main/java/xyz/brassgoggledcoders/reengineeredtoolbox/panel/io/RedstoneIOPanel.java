package xyz.brassgoggledcoders.reengineeredtoolbox.panel.io;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.panel.RedstoneIOPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneIOPanelEntity;

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
    @NotNull
    public InteractionResult use(IFrameEntity frameEntity, PanelState panelState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (frameEntity.getPanelEntity(pHit.getDirection()) instanceof RedstoneIOPanelEntity redstoneIOPanelEntity) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                frameEntity.openMenu(
                        serverPlayer,
                        redstoneIOPanelEntity,
                        new SimpleMenuProvider(
                                (menuId, inventory, player) -> new RedstoneIOPanelMenu(
                                        menuId,
                                        inventory,
                                        redstoneIOPanelEntity.getSlotForMenu(),
                                        ContainerLevelAccess.create(frameEntity.getFrameLevel(), frameEntity.getFramePos()),
                                        panelState.getFacing(),
                                        panelState.getPanel()
                                ),
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

    @Override
    public boolean canConnectRedstone(IFrameEntity entity, PanelState panelState) {
        return true;
    }
}
