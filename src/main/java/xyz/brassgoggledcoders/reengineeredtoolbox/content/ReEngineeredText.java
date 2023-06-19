package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.network.chat.Component;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

public class ReEngineeredText {

    public static final Component REDSTONE_SLOT_IN = frameSlotLang("redstone_in", "Redstone Input");
    public static final Component REDSTONE_SLOT_OUT = frameSlotLang("redstone_out", "Redstone Output");
    public static final Component REDSTONE_SLOT_INVERTED_OUT = frameSlotLang("redstone_inverted_out", "Redstone Output (Inverted)");

    public static final Component ITEM_SLOT_IN = frameSlotLang("item_in", "Item Input");
    public static final Component ITEM_SLOT_OUT = frameSlotLang("item_out", "Item Output");

    public static void setup() {

    }

    private static Component frameSlotLang(String lang, String name) {
        return ReEngineeredToolbox.getRegistrate()
                .addLang("frame_slot", ReEngineeredToolbox.rl(lang), name);
    }

}
