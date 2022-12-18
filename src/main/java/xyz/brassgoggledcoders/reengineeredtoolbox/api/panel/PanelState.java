package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
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
    public PanelEntity createPanelEntity(IFrameEntity frame) {
        return this.getPanel().createPanelEntity(this, frame);
    }
}
