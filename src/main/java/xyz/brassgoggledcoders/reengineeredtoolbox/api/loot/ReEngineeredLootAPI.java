package xyz.brassgoggledcoders.reengineeredtoolbox.api.loot;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredToolboxAPI;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.Objects;
import java.util.function.Supplier;

public class ReEngineeredLootAPI {
    private static final Supplier<LootContextParamSet> LOOT_PARAMETER_SET = Suppliers.memoize(() -> LootContextParamSets.get(
            new ResourceLocation(ReEngineeredToolboxAPI.ID, "panel")
    ));

    public static LootContextParam<PanelState> PANELSTATE = new LootContextParam<>(new ResourceLocation(
            ReEngineeredToolboxAPI.ID,
            "panelstate"
    ));

    public static LootContextParam<PanelEntity> PANEL_ENTITY = new LootContextParam<>(new ResourceLocation(
            ReEngineeredToolboxAPI.ID,
            "panel_entity"
    ));

    public static LootContextParamSet getPanelLootParamSet() {
        return Objects.requireNonNull(LOOT_PARAMETER_SET.get(), "Failed to find Panel Loot Parameter Set");
    }

}
