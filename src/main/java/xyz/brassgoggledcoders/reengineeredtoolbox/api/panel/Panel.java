package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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

    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("block", ReEngineeredPanels.getRegistry().getKey(this));
        }

        return this.descriptionId;
    }

    private boolean filter(Item item) {
        return item instanceof PanelLike panelLike && panelLike.asPanel() == this;
    }
}
