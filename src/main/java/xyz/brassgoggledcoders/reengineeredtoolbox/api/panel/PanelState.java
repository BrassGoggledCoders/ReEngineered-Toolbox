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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelEntityPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction.InteractionPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone.RedstonePanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.FacingPropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.Iterator;
import java.util.Optional;

public class PanelState extends StateHolder<Panel, PanelState> {
    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
    }

    public Panel getPanel() {
        return this.owner;
    }

    public PanelState withDirection(Direction direction) {
        FacingPropertyComponent component = this.getPanel().getComponent(FacingPropertyComponent.class);
        if (component != null) {
            return this.setValue(component.getProperty(), direction);
        }
        return this;
    }

    @Nullable
    public Direction getFacing() {
        FacingPropertyComponent component = this.getPanel().getComponent(FacingPropertyComponent.class);
        if (component != null) {
            return this.getValue(component.getProperty());
        }
        return null;
    }

    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame) {
        return Optional.ofNullable(this.getPanel().getComponent(PanelEntityPanelComponent.class))
                .map(component -> component.createPanelEntity(frame, this))
                .orElse(null);
    }

    public boolean canConnectRedstone(IFrameEntity frame) {
        for (RedstonePanelComponent component : this.getPanel().getComponents(RedstonePanelComponent.class)) {
            if (component.canConnectRedstone(frame, this)) {
                return true;
            }
        }

        return false;
    }

    public boolean is(Panel panel) {
        return this.getPanel() == panel;
    }

    public InteractionResult use(IFrameEntity frameEntity, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = InteractionResult.PASS;
        Iterator<InteractionPanelComponent> iterator = this.getPanel().getComponents(InteractionPanelComponent.class).iterator();

        while (result == InteractionResult.PASS && iterator.hasNext()) {
            result = iterator.next().use(frameEntity, this, pPlayer, pHand, pHit);
        }

        return result;
    }

    public int getSignal(IFrameEntity frameEntity) {
        int signal = 0;
        for (RedstonePanelComponent component : this.getPanel().getComponents(RedstonePanelComponent.class)) {
            int componentSignal = component.getSignal(frameEntity, this);
            if (componentSignal > signal) {
                signal = componentSignal;
            }
        }
        return signal;
    }
}
