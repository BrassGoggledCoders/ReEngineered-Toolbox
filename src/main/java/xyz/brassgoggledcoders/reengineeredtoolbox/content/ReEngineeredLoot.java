package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.loot.ReEngineeredLootAPI;
import xyz.brassgoggledcoders.reengineeredtoolbox.mixin.LootContextParamSetsAccessor;

public class ReEngineeredLoot {

    public static LootContextParamSet PANEL_LOOT_SET = LootContextParamSetsAccessor.callRegister(
            ReEngineeredToolbox.rl("panel").toString(),
            builder -> builder.required(ReEngineeredLootAPI.PANELSTATE)
                    .optional(ReEngineeredLootAPI.PANEL_ENTITY)
                    .required(LootContextParams.ORIGIN)
                    .optional(LootContextParams.BLOCK_STATE)
                    .optional(LootContextParams.BLOCK_ENTITY)
                    .optional(LootContextParams.TOOL)
                    .optional(LootContextParams.THIS_ENTITY)
                    .optional(LootContextParams.KILLER_ENTITY)
    );

    public static void setup() {

    }
}
