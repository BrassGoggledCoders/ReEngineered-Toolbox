package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.PanelItem;

public class RETItems {
    public static final ItemEntry<PanelItem> PANEL = ReEngineeredToolbox.getRegistrate()
            .object("panel")
            .item(PanelItem::new)
            .register();

    public static void setup() {

    }
}
