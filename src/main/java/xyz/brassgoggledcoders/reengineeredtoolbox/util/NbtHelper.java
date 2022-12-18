package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NbtHelper {
    public static PanelState readPanelState(CompoundTag pTag) {
        if (pTag.contains("Name", 8)) {
            Panel panel = ReEngineeredPanels.getRegistry().getValue(new ResourceLocation(pTag.getString("Name")));
            if (panel != null) {
                PanelState panelState = panel.defaultPanelState();
                if (pTag.contains("Properties", 10)) {
                    CompoundTag compoundtag = pTag.getCompound("Properties");
                    StateDefinition<Panel, PanelState> statedefinition = panel.getStateDefinition();

                    for (String s : compoundtag.getAllKeys()) {
                        Property<?> property = statedefinition.getProperty(s);
                        if (property != null) {
                            panelState = setValueHelper(panelState, property, s, compoundtag, pTag);
                        }
                    }
                }
                return panelState;
            }
        }
        return ReEngineeredPanels.PLUG.getDefaultState();
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S pStateHolder, Property<T> pProperty, String pPropertyName, CompoundTag pPropertiesTag, CompoundTag pBlockStateTag) {
        Optional<T> optional = pProperty.getValue(pPropertiesTag.getString(pPropertyName));
        if (optional.isPresent()) {
            return pStateHolder.setValue(pProperty, optional.get());
        } else {
            ReEngineeredToolbox.LOGGER.warn("Unable to read property: {} with value: {} for panelstate: {}", pPropertyName, pPropertiesTag.getString(pPropertyName), pBlockStateTag.toString());
            return pStateHolder;
        }
    }

    public static CompoundTag writePanelState(PanelState pState) {
        CompoundTag panelStateTag = new CompoundTag();
        panelStateTag.putString("Name", Objects.requireNonNull(ReEngineeredPanels.getRegistry().getKey(pState.getPanel())).toString());
        ImmutableMap<Property<?>, Comparable<?>> immutablemap = pState.getValues();
        if (!immutablemap.isEmpty()) {
            CompoundTag propertyTag = new CompoundTag();

            for (Map.Entry<Property<?>, Comparable<?>> entry : immutablemap.entrySet()) {
                Property<?> property = entry.getKey();
                propertyTag.putString(property.getName(), getName(property, entry.getValue()));
            }

            panelStateTag.put("Properties", propertyTag);
        }

        return panelStateTag;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getName(Property<T> pProperty, Comparable<?> pValue) {
        return pProperty.getName((T) pValue);
    }
}
