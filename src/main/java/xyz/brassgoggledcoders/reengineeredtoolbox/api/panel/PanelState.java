package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.base.Suppliers;
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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction.IInteractionPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.panelentity.IPanelEntityPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone.IRedstonePanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.FacingPropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.PanelStatePropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class PanelState extends StateHolder<Panel, PanelState> {
    private final Supplier<Property<Direction>> facingProperty;

    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
        this.facingProperty = Suppliers.memoize(() -> Optional.ofNullable(this.getPanel()
                        .getComponent(FacingPropertyComponent.class)
                )
                .map(PanelStatePropertyComponent::getProperty)
                .orElse(null)
        );
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
    public Property<Direction> getFacingProperty() {
        return this.facingProperty.get();
    }

    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame) {
        return Optional.ofNullable(this.getPanel().getComponent(IPanelEntityPanelComponent.class))
                .map(component -> component.createPanelEntity(frame, this))
                .orElse(null);
    }

    public boolean canConnectRedstone(IFrameEntity frame, IPanelPosition panelPosition) {
        for (IRedstonePanelComponent component : this.getPanel().getComponents(IRedstonePanelComponent.class)) {
            if (component.canConnectRedstone(frame, this, panelPosition)) {
                return true;
            }
        }

        return false;
    }

    public boolean is(Panel panel) {
        return this.getPanel() == panel;
    }

    public InteractionResult use(IFrameEntity frameEntity, IPanelPosition panelPosition, Player pPlayer,
                                 InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = InteractionResult.PASS;
        Iterator<IInteractionPanelComponent> iterator = this.getPanel()
                .getComponents(IInteractionPanelComponent.class)
                .iterator();

        while (result == InteractionResult.PASS && iterator.hasNext()) {
            result = iterator.next().use(this, frameEntity, panelPosition, pPlayer, pHand, pHit);
        }

        return result;
    }

    public int getSignal(IFrameEntity frameEntity, IPanelPosition panelPosition) {
        int signal = 0;
        for (IRedstonePanelComponent component : this.getPanel().getComponents(IRedstonePanelComponent.class)) {
            int componentSignal = component.getSignal(frameEntity, this, panelPosition);
            if (componentSignal > signal) {
                signal = componentSignal;
            }
        }
        return signal;
    }
}
