package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty;

import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

public interface IStatePropertyPanelComponent<T extends Comparable<T>> {

    @NotNull
    Property<T> getProperty();

    @Nullable
    T getDefaultValue();

    @NotNull
    default PanelState setValueToPanelState(@NotNull PanelState panelState) {
        if (this.getDefaultValue() != null) {
            return panelState.setValue(this.getProperty(), this.getDefaultValue());
        }

        return panelState;
    }
}
