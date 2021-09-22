package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.PanelBuilder;

public class RETPanels {

    public static final RegistryEntry<Panel> OPEN = ReEngineeredToolbox.getRegistrate()
            .object("open")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> BLANK = ReEngineeredToolbox.getRegistrate()
            .object("blank")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static void setup() {

    }
}
