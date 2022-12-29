package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public class PanelState extends StateHolder<Panel, PanelState> {
    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
    }

    public Panel getPanel() {
        return this.owner;
    }

    public PanelState withDirection(Direction direction) {
        if (this.getPanel().getFacingProperty() != null) {
            return this.setValue(this.getPanel().getFacingProperty(), direction);
        }
        return this;
    }

    @Nullable
    public Direction getFacing() {
        return this.getPanel().getFacing(this);
    }

    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame) {
        return this.getPanel().createPanelEntity(frame, this);
    }

    public boolean canConnectRedstone(IFrameEntity frame) {
        return this.getPanel().canConnectRedstone(frame, this);
    }

    public boolean is(Panel panel) {
        return this.getPanel() == panel;
    }

    public InteractionResult use(IFrameEntity frameEntity, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return this.getPanel().use(frameEntity, this, pPlayer, pHand, pHit);
    }
}
