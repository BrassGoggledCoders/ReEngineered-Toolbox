package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider.LootType;
import com.tterrag.registrate.providers.loot.RegistrateLootTables;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.loot.PanelLoot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.loot.ReEngineeredLootAPI;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistratePanelLoot extends PanelLoot implements RegistrateLootTables {
    public static final LootType<RegistratePanelLoot> TYPE = LootType.register(
            "panel",
            ReEngineeredLootAPI.getPanelLootParamSet(),
            RegistratePanelLoot::new
    );

    private final AbstractRegistrate<?> parent;
    private final Consumer<RegistratePanelLoot> callback;

    public RegistratePanelLoot(AbstractRegistrate<?> registrate, Consumer<RegistratePanelLoot> consumer) {
        this.parent = registrate;
        this.callback = consumer;
    }

    @Override
    public void addTables() {
        this.callback.accept(this);
    }

    @Override
    protected Iterable<Panel> getKnownPanels() {
        return parent.getAll(ReEngineeredPanels.PANEL_KEY)
                .stream()
                .map(Supplier::get)
                .toList();
    }
}
