package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.Optional;

public class Panel implements ItemLike {
    @Nullable
    private String descriptionId;
    @Nullable
    private Item item;
    @NotNull
    private final StateDefinition<Panel, PanelState> stateDefinition;
    @NotNull
    private PanelState defaultPanelState;

    public Panel() {
        StateDefinition.Builder<Panel, PanelState> builder = new StateDefinition.Builder<>(this);
        this.createPanelStateDefinition(builder);
        this.stateDefinition = builder.create(Panel::defaultPanelState, PanelState::new);
        this.defaultPanelState = this.stateDefinition.any();
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
            this.descriptionId = Util.makeDescriptionId("block", ReEngineeredPanels.getRegistry().getKey(this));
        }

        return this.descriptionId;
    }

    public PanelState getPanelStateForPlacement(UseOnContext context) {
        PanelState panelState = this.defaultPanelState();
        if (this.getFacingProperty() != null) {
            panelState = panelState.setValue(this.getFacingProperty(), context.getClickedFace());
        }
        return panelState;
    }

    private boolean filter(Item item) {
        return item instanceof PanelLike panelLike && panelLike.asPanel() == this;
    }

    @Nullable
    public Property<Direction> getFacingProperty() {
        return BlockStateProperties.FACING;
    }

    protected void createPanelStateDefinition(StateDefinition.Builder<Panel, PanelState> pBuilder) {
        if (this.getFacingProperty() != null) {
            pBuilder.add(this.getFacingProperty());
        }
    }

    @NotNull
    public StateDefinition<Panel, PanelState> getStateDefinition() {
        return this.stateDefinition;
    }

    protected final void registerDefaultState(@NotNull PanelState pState) {
        this.defaultPanelState = pState;
    }

    /**
     * Gets the default state for this block
     */
    public final PanelState defaultPanelState() {
        return this.defaultPanelState;
    }
}
