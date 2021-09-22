package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;

import javax.annotation.Nullable;

public class Panel extends ForgeRegistryEntry<Panel> {
    private final LazyValue<String> descriptionId;
    private final StateContainer<Panel, PanelState> stateContainer;
    private final PanelState defaultState;

    public Panel() {
        this.descriptionId = new LazyValue<>(() -> Util.makeDescriptionId("panel", this.getRegistryName()));
        StateContainer.Builder<Panel, PanelState> builder = new StateContainer.Builder<>(this);
        this.createStateDefinition(builder);
        this.stateContainer = builder.create(Panel::getDefaultState, PanelState::new);
        this.defaultState = this.createDefaultState();
    }

    public String getDescriptionId() {
        return this.descriptionId.get();
    }

    public IFormattableTextComponent getName() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    public boolean isStateValidFor(PanelState panelState, Direction direction) {
        return true;
    }

    public PanelState getStateForPlacement(PanelState panelState, IPanelPlacement panelPlacement, IFrame frame) {
        if (panelState.isValidFor(panelPlacement.getPlacementFace())) {
            return panelState;
        } else {
            return null;
        }
    }

    @Nullable
    public PanelEntity createPanelEntity(PanelState panelState) {
        return null;
    }

    public PanelState getDefaultState() {
        return defaultState;
    }

    public StateContainer<Panel, PanelState> getStateContainer() {
        return stateContainer;
    }

    protected void createStateDefinition(StateContainer.Builder<Panel, PanelState> builder) {

    }

    protected PanelState createDefaultState() {
        return this.stateContainer.any();
    }
}
