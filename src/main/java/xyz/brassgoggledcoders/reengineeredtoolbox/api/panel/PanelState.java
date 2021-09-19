package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.state.Property;
import net.minecraft.state.StateHolder;

public class PanelState extends StateHolder<Panel, PanelState> {
    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
    }
}
