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

    public static final RegistryEntry<Panel> DISPENSER = ReEngineeredToolbox.getRegistrate()
            .object("dispenser")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> FLUID_INPUT = ReEngineeredToolbox.getRegistrate()
            .object("fluid_input")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> FLUID_OUTPUT = ReEngineeredToolbox.getRegistrate()
            .object("fluid_output")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> ENERGY_INPUT = ReEngineeredToolbox.getRegistrate()
            .object("energy_input")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> ENERGY_OUTPUT = ReEngineeredToolbox.getRegistrate()
            .object("energy_output")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> ITEM_INPUT = ReEngineeredToolbox.getRegistrate()
            .object("item_input")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();

    public static final RegistryEntry<Panel> ITEM_OUTPUT = ReEngineeredToolbox.getRegistrate()
            .object("item_output")
            .entry(PanelBuilder.entry())
            .panel(Panel::new)
            .register();


    public static void setup() {

    }
}
