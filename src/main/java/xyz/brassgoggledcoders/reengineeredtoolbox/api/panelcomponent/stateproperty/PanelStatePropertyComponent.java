package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty;

import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

public class PanelStatePropertyComponent<T extends Comparable<T>> extends PanelComponent implements IStatePropertyPanelComponent<T> {
    @NotNull
    private final Property<T> property;
    @Nullable
    private final T defaultValue;

    public PanelStatePropertyComponent(@NotNull Property<T> property) {
        this(property, null);
    }

    public PanelStatePropertyComponent(@NotNull Property<T> property, @Nullable T defaultValue) {
        this.property = property;
        this.defaultValue = defaultValue;
    }

    @NotNull
    @Override
    public Property<T> getProperty() {
        return property;
    }

    @Override
    @Nullable
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    @NotNull
    public PanelState setValueToPanelState(@NotNull PanelState panelState) {
        if (this.getDefaultValue() != null) {
            return panelState.setValue(this.getProperty(), this.getDefaultValue());
        }

        return panelState;
    }
}
