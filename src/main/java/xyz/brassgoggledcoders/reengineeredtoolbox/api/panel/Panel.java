package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.PanelStatePropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Panel implements ItemLike {
    @Nullable
    private String descriptionId;
    @Nullable
    private Item item;
    @NotNull
    private final StateDefinition<Panel, PanelState> stateDefinition;
    @NotNull
    private final PanelState defaultPanelState;
    @NotNull
    private final List<PanelComponent> components;
    @NotNull
    private final ListMultimap<Class<?>, PanelComponent> cachedComponents;


    public Panel(Collection<PanelComponent> components) {
        this.components = new ArrayList<>(components);
        this.cachedComponents = MultimapBuilder.hashKeys()
                .arrayListValues()
                .build();

        StateDefinition.Builder<Panel, PanelState> builder = new StateDefinition.Builder<>(this);
        for (PanelStatePropertyComponent<?> propertyComponent : this.getComponents(PanelStatePropertyComponent.class)) {
            builder.add(propertyComponent.getProperty());
        }
        this.stateDefinition = builder.create(Panel::defaultPanelState, PanelState::new);

        PanelState panelState = this.stateDefinition.any();
        for (PanelStatePropertyComponent<?> propertyComponent : this.getComponents(PanelStatePropertyComponent.class)) {
            panelState = propertyComponent.setValueToPanelState(panelState);
        }
        this.defaultPanelState = panelState;
    }

    @Override
    @NotNull
    public Item asItem() {
        if (this.item == null) {
            ResourceLocation panelKey = ReEngineeredPanels.getRegistry()
                    .getKey(this);
            this.item = Optional.ofNullable(ForgeRegistries.ITEMS.getValue(panelKey))
                    .filter(this::filter)
                    .or(() -> ForgeRegistries.ITEMS.getValues()
                            .stream()
                            .filter(this::filter)
                            .findFirst()
                    ).orElse(Items.AIR);
        }
        return this.item;
    }

    @NotNull
    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("panel", ReEngineeredPanels.getRegistry().getKey(this));
        }

        return this.descriptionId;
    }

    private boolean filter(Item item) {
        return item instanceof PanelLike panelLike && panelLike.asPanel() == this;
    }

    @NotNull
    public StateDefinition<Panel, PanelState> getStateDefinition() {
        return this.stateDefinition;
    }


    /**
     * Gets the default state for this block
     */
    public final PanelState defaultPanelState() {
        return this.defaultPanelState;
    }

    @Nullable
    public <T extends PanelComponent> T getComponent(Class<T> clazz) {
        List<T> components = this.getComponents(clazz);
        return components.isEmpty() ? null : components.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends PanelComponent> List<T> getComponents(Class<T> clazz) {
        if (this.cachedComponents.containsKey(clazz)) {
            return (List<T>) this.cachedComponents.get(clazz);
        }

        for (PanelComponent panelComponent : this.components) {
            if (clazz.isInstance(panelComponent)) {
                this.cachedComponents.put(clazz, panelComponent);
            }
        }

        return (List<T>) this.cachedComponents.get(clazz);
    }

}
