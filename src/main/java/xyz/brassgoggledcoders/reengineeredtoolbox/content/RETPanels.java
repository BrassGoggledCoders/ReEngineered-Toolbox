package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

public class RETPanels {

    public static final RegistryEntry<Panel> BLANK = ReEngineeredToolbox.getRegistrate()
            .object("blank")
            .simple(Panel.class, Panel::new);

    public static void setup() {

    }
}
