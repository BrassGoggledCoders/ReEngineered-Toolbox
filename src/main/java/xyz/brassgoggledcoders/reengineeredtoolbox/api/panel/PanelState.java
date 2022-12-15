package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

public class PanelState extends StateHolder<Panel, PanelState> {
    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
    }

    public Panel getPanel() {
        return this.owner;
    }

    public boolean isValidFor(Direction direction) {
        return !this.hasProperty(PanelStateProperties.FACING_DIRECTION) ||
                this.getValue(PanelStateProperties.FACING_DIRECTION) == direction;
    }
}