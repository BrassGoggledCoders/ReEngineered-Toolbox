package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateHolder;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NBTHelper {
    public static PanelState readPanelState(CompoundNBT panelStateNBT) {
        if (panelStateNBT.contains("name", 8)) {
            Panel panel = RETRegistries.PANELS.get().getValue(new ResourceLocation(panelStateNBT.getString("name")));
            if (panel != null) {
                PanelState panelState = panel.getDefaultState();
                if (panelStateNBT.contains("properties", 10)) {
                    CompoundNBT compoundnbt = panelStateNBT.getCompound("properties");
                    StateContainer<Panel, PanelState> stateContainer = panel.getStateContainer();

                    for (String s : compoundnbt.getAllKeys()) {
                        Property<?> property = stateContainer.getProperty(s);
                        if (property != null) {
                            panelState = setValueHelper(panelState, property, s, compoundnbt, panelStateNBT);
                        }
                    }
                }
                return panelState;
            }
        }
        return RETPanels.OPEN.get()
                .getDefaultState();
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(
            S stateHolder, Property<T> property, String propertyName, CompoundNBT propertiesNBT,
            CompoundNBT panelStateNBT) {
        Optional<T> optional = property.getValue(propertiesNBT.getString(propertyName));
        if (optional.isPresent()) {
            return stateHolder.setValue(property, optional.get());
        } else {
            ReEngineeredToolbox.LOGGER.warn("Unable to read property: {} with value: {} for panelstate: {}",
                    propertyName, propertiesNBT.getString(propertyName), panelStateNBT.toString());
            return stateHolder;
        }
    }

    /**
     * Writes the given blockstate to the given tag.
     */
    public static CompoundNBT writePanelState(PanelState pState) {
        CompoundNBT panelStateNBT = new CompoundNBT();
        panelStateNBT.putString("name", Objects.requireNonNull(pState.getPanel().getRegistryName()).toString());
        ImmutableMap<Property<?>, Comparable<?>> stateValues = pState.getValues();
        if (!stateValues.isEmpty()) {
            CompoundNBT propertiesNBT = new CompoundNBT();

            for (Map.Entry<Property<?>, Comparable<?>> entry : stateValues.entrySet()) {
                Property<?> property = entry.getKey();
                propertiesNBT.putString(property.getName(), getName(property, entry.getValue()));
            }

            panelStateNBT.put("properties", propertiesNBT);
        }

        return panelStateNBT;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getName(Property<T> pProperty, Comparable<?> pValue) {
        return pProperty.getName((T) pValue);
    }

}
