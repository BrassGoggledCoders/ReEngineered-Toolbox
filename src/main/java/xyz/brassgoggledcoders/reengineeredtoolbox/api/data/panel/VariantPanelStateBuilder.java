/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package xyz.brassgoggledcoders.reengineeredtoolbox.api.data.panel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.state.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider.ConfiguredModelList;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

public class VariantPanelStateBuilder implements IGeneratedPanelState {

    private final Panel owner;
    private final Map<VariantPanelStateBuilder.PartialPanelstate, ConfiguredModelList> models = new LinkedHashMap<>();
    private final Set<PanelState> coveredStates = new HashSet<>();

    VariantPanelStateBuilder(Panel owner) {
        this.owner = owner;
    }

    public Map<VariantPanelStateBuilder.PartialPanelstate, ConfiguredModelList> getModels() {
        return models;
    }

    @Override
    public Panel getOwner() {
        return owner;
    }

    @Override
    public JsonElement get() {
        List<PanelState> missingStates = Lists.newArrayList(owner.getStateContainer().getPossibleStates());
        missingStates.removeAll(coveredStates);
        Preconditions.checkState(missingStates.isEmpty(), "Panelstate for panel %s does not cover all states. Missing: %s", owner, missingStates);
        JsonObject variants = new JsonObject();
        getModels().entrySet().stream()
                .sorted(Entry.comparingByKey(VariantPanelStateBuilder.PartialPanelstate.comparingByProperties()))
                .forEach(entry -> variants.add(entry.getKey().toString(), entry.getValue().toJSON()));
        JsonObject main = new JsonObject();
        main.add("variants", variants);
        return main;
    }

    public VariantPanelStateBuilder addModels(VariantPanelStateBuilder.PartialPanelstate state, ConfiguredModel... models) {
        Preconditions.checkNotNull(state, "state must not be null");
        Preconditions.checkArgument(models.length > 0, "Cannot set models to empty array");
        Preconditions.checkArgument(state.getOwner() == owner, "Cannot set models for a different panel. Found: %s, Current: %s", state.getOwner(), owner);
        if (!this.models.containsKey(state)) {
            Preconditions.checkArgument(disjointToAll(state), "Cannot set models for a state for which a partial match has already been configured");
            this.models.put(state, new ConfiguredModelList(models));
            for (PanelState fullState : owner.getStateContainer().getPossibleStates()) {
                if (state.test(fullState)) {
                    coveredStates.add(fullState);
                }
            }
        } else {
            this.models.computeIfPresent(state, ($, cml) -> cml.append(models));
        }
        return this;
    }

    public VariantPanelStateBuilder setModels(VariantPanelStateBuilder.PartialPanelstate state, ConfiguredModel... model) {
        Preconditions.checkArgument(!models.containsKey(state), "Cannot set models for a state that has already been configured: %s", state);
        addModels(state, model);
        return this;
    }

    private boolean disjointToAll(VariantPanelStateBuilder.PartialPanelstate newState) {
        return coveredStates.stream().noneMatch(newState);
    }

    public VariantPanelStateBuilder.PartialPanelstate partialState() {
        return new VariantPanelStateBuilder.PartialPanelstate(owner, this);
    }

    public VariantPanelStateBuilder forAllStates(Function<PanelState, ConfiguredModel[]> mapper) {
        return forAllStatesExcept(mapper);
    }

    public VariantPanelStateBuilder forAllStatesExcept(Function<PanelState, ConfiguredModel[]> mapper, Property<?>... ignored) {
        Set<VariantPanelStateBuilder.PartialPanelstate> seen = new HashSet<>();
        for (PanelState fullState : owner.getStateContainer().getPossibleStates()) {
            Map<Property<?>, Comparable<?>> propertyValues = Maps.newLinkedHashMap(fullState.getValues());
            for (Property<?> p : ignored) {
                propertyValues.remove(p);
            }
            VariantPanelStateBuilder.PartialPanelstate partialState = new VariantPanelStateBuilder.PartialPanelstate(owner, propertyValues, this);
            if (seen.add(partialState)) {
                setModels(partialState, mapper.apply(fullState));
            }
        }
        return this;
    }

    public static class PartialPanelstate implements Predicate<PanelState> {
        private final Panel owner;
        private final SortedMap<Property<?>, Comparable<?>> setStates;
        private final VariantPanelStateBuilder outerBuilder;

        PartialPanelstate(Panel owner, VariantPanelStateBuilder outerBuilder) {
            this(owner, ImmutableMap.of(), outerBuilder);
        }

        PartialPanelstate(Panel owner, Map<Property<?>, Comparable<?>> setStates, @Nonnull VariantPanelStateBuilder outerBuilder) {
            this.owner = owner;
            this.outerBuilder = outerBuilder;
            for (Map.Entry<Property<?>, Comparable<?>> entry : setStates.entrySet()) {
                Property<?> prop = entry.getKey();
                Comparable<?> value = entry.getValue();
                Preconditions.checkArgument(owner.getStateContainer().getProperties().contains(prop), "Property %s not found on panel %s", entry, this.owner);
                Preconditions.checkArgument(prop.getPossibleValues().contains(value), "%s is not a valid value for %s", value, prop);
            }
            this.setStates = Maps.newTreeMap(Comparator.comparing(Property::getName));
            this.setStates.putAll(setStates);
        }

        public <T extends Comparable<T>> VariantPanelStateBuilder.PartialPanelstate with(Property<T> prop, T value) {
            Preconditions.checkArgument(!setStates.containsKey(prop), "Property %s has already been set", prop);
            Map<Property<?>, Comparable<?>> newState = new HashMap<>(setStates);
            newState.put(prop, value);
            return new VariantPanelStateBuilder.PartialPanelstate(owner, newState, outerBuilder);
        }

        private void checkValidOwner() {
            Preconditions.checkNotNull(outerBuilder, "Partial panelstate must have a valid owner to perform this action");
        }

        /**
         * Add models to the current state's variant. For use when it is more convenient
         * to add multiple sets of models, as a replacement for
         * {@link #setModels(ConfiguredModel...)}.
         *
         * @param models The models to add.
         * @return {@code this}
         * @throws NullPointerException If the parent builder is {@code null}
         */
        public VariantPanelStateBuilder.PartialPanelstate addModels(ConfiguredModel... models) {
            checkValidOwner();
            outerBuilder.addModels(this, models);
            return this;
        }

        /**
         * Set this variant's models, and return the parent builder.
         *
         * @param models The models to set
         * @return The parent builder instance
         * @throws NullPointerException If the parent builder is {@code null}
         */
        public VariantPanelStateBuilder setModels(ConfiguredModel... models) {
            checkValidOwner();
            return outerBuilder.setModels(this, models);
        }

        /**
         * Complete this state without adding any new models, and return a new partial
         * state via the parent builder. For use after calling
         * {@link #addModels(ConfiguredModel...)}.
         *
         * @return A fresh partial state as specified by
         * {@link VariantPanelStateBuilder#partialState()}.
         * @throws NullPointerException If the parent builder is {@code null}
         */
        public VariantPanelStateBuilder.PartialPanelstate partialState() {
            checkValidOwner();
            return outerBuilder.partialState();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VariantPanelStateBuilder.PartialPanelstate that = (VariantPanelStateBuilder.PartialPanelstate) o;
            return owner.equals(that.owner) &&
                    setStates.equals(that.setStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(owner, setStates);
        }

        public Panel getOwner() {
            return owner;
        }

        public SortedMap<Property<?>, Comparable<?>> getSetStates() {
            return setStates;
        }

        @Override
        public boolean test(PanelState panelState) {
            if (panelState.getPanel() != getOwner()) {
                return false;
            }
            for (Map.Entry<Property<?>, Comparable<?>> entry : setStates.entrySet()) {
                if (panelState.getValue(entry.getKey()) != entry.getValue()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public String toString() {
            StringBuilder ret = new StringBuilder();
            for (Map.Entry<Property<?>, Comparable<?>> entry : setStates.entrySet()) {
                if (ret.length() > 0) {
                    ret.append(',');
                }
                ret.append(entry.getKey().getName())
                        .append('=')
                        .append(((Property) entry.getKey()).getName(entry.getValue()));
            }
            return ret.toString();
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public static Comparator<VariantPanelStateBuilder.PartialPanelstate> comparingByProperties() {
            // Sort variants inversely by property values, to approximate vanilla style
            return (s1, s2) -> {
                SortedSet<Property<?>> propUniverse = new TreeSet<>(s1.getSetStates().comparator().reversed());
                propUniverse.addAll(s1.getSetStates().keySet());
                propUniverse.addAll(s2.getSetStates().keySet());
                for (Property<?> prop : propUniverse) {
                    Comparable val1 = s1.getSetStates().get(prop);
                    Comparable val2 = s2.getSetStates().get(prop);
                    if (val1 == null && val2 != null) {
                        return -1;
                    } else if (val2 == null && val1 != null) {
                        return 1;
                    } else if (val1 != null) {
                        int cmp = val1.compareTo(val2);
                        if (cmp != 0) {
                            return cmp;
                        }
                    }
                }
                return 0;
            };
        }
    }
}

