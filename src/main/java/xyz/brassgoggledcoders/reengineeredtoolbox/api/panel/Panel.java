package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.state.StateContainer;
import net.minecraft.util.LazyValue;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
