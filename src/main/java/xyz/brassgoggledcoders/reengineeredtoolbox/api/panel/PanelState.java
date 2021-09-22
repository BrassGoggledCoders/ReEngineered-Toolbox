package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.state.Property;
import net.minecraft.state.StateHolder;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PanelState extends StateHolder<Panel, PanelState> {
    public PanelState(Panel pOwner, ImmutableMap<Property<?>, Comparable<?>> pValues, MapCodec<PanelState> pPropertiesCodec) {
        super(pOwner, pValues, pPropertiesCodec);
    }

    public boolean isValidFor(Direction direction) {
        return this.getPanel()
                .isStateValidFor(this, direction);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public PanelState getStateForPlacement(IPanelPlacement panelPlacement, IFrame frame) {
        return this.getPanel()
                .getStateForPlacement(this, panelPlacement, frame);
    }

    @Nullable
    public PanelEntity createPanelEntity() {
        return this.getPanel().createPanelEntity(this);
    }

    public Panel getPanel() {
        return this.owner;
    }


}
