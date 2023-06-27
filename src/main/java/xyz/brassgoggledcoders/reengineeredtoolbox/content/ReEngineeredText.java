package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.network.chat.Component;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

public class ReEngineeredText {

    public static final Component REDSTONE_SLOT_IN = frameSlotLang("redstone_in", "Redstone Input");
    public static final Component REDSTONE_SLOT_OUT = frameSlotLang("redstone_out", "Redstone Output");
    public static final Component REDSTONE_SLOT_INVERTED_OUT = frameSlotLang("redstone_inverted_out", "Redstone Output (Inverted)");

    public static final Component ITEM_SLOT_IN = frameSlotLang("item_in", "Item Input");
    public static final Component ITEM_SLOT_OUT = frameSlotLang("item_out", "Item Output");

    public static final Component FLUID_SLOT_IN = frameSlotLang("fluid_in", "Fluid Input");
    public static final Component FLUID_SLOT_OUT = frameSlotLang("fluid_out", "Fluid Output");

    public static final Component ENERGY_SLOT_IN = frameSlotLang("energy_in", "Energy Input");
    public static final Component ENERGY_SLOT_OUT = frameSlotLang("energy_out", "Energy Output");

    public static void setup() {

    }

    private static Component frameSlotLang(String lang, String name) {
        return ReEngineeredToolbox.getRegistrate()
                .addLang("frame_slot", ReEngineeredToolbox.rl(lang), name);
    }

}
