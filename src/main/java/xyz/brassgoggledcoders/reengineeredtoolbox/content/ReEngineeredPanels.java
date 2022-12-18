package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.PanelEntry;

import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ReEngineeredPanels {
    public static final ResourceKey<Registry<Panel>> PANEL_KEY = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("panel", RegistryBuilder::new);

    public static final Supplier<ForgeRegistry<Panel>> PANEL_REGISTRY = Suppliers.memoize(() ->
            RegistryManager.ACTIVE.getRegistry(PANEL_KEY)
    );

    @SuppressWarnings("DataFlowIssue")
    public static final PanelEntry<Panel> PLUG = ReEngineeredToolbox.getRegistrateAddon()
            .object("plug")
            .panel(Panel::new)
            .panelState((context, provider) -> provider.openDirectionalPanel(context.get()))
            .item()
            .properties(properties -> properties.tab(null))
            .build()
            .register();

    public static final PanelEntry<Panel> ITEM_OUTPUT = ReEngineeredToolbox.getRegistrateAddon()
            .object("item_output")
            .panel(Panel::new)
            .item()
            .build()
            .register();


    public static ForgeRegistry<Panel> getRegistry() {
        return PANEL_REGISTRY.get();
    }

    public static void setup() {

    }
}
