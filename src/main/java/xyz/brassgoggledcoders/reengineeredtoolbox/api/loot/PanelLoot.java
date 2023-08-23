package xyz.brassgoggledcoders.reengineeredtoolbox.api.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PanelLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();

    public abstract void addTables();

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> resourceLocationBuilderBiConsumer) {
        this.addTables();
        Set<ResourceLocation> set = Sets.newHashSet();

        for (Panel panel : getKnownPanels()) {
            ResourceLocation resourcelocation = panel.getLootTable();
            if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                LootTable.Builder lootTableBuilder = this.map.remove(resourcelocation);
                if (lootTableBuilder == null) {
                    throw new IllegalStateException(String.format(Locale.ROOT, "Missing loot table '%s' for '%s'", resourcelocation, ReEngineeredPanels.PANEL_REGISTRY.get().getKey(panel)));
                }

                resourceLocationBuilderBiConsumer.accept(resourcelocation, lootTableBuilder);
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-panels: " + this.map.keySet());
        }
    }

    public void dropSelf(Panel panel) {
        this.dropOther(panel, panel);
    }

    public void dropOther(Panel panel, ItemLike itemLike) {
        this.add(panel, createSingleItemTable(itemLike));
    }

    public static LootTable.Builder createSingleItemTable(ItemLike itemLike) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(itemLike))
                );
    }

    public void add(Panel panel, Function<Panel, LootTable.Builder> factory) {
        this.add(panel, factory.apply(panel));
    }

    public void add(Panel panel, LootTable.Builder builder) {
        this.map.put(panel.getLootTable(), builder);
    }

    protected Iterable<Panel> getKnownPanels() {
        return ReEngineeredPanels.getRegistry()
                .getValues();
    }
}
