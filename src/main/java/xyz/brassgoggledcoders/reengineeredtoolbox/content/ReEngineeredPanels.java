package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.io.RedstoneIOPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.RedstoneInputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.PanelEntry;

import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ReEngineeredPanels {
    public static final ResourceKey<Registry<Panel>> PANEL_KEY = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("panel", RegistryBuilder::new);

    public static final ResourceKey<Registry<PanelEntityType<?>>> PANEL_ENTITY_KEY = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("panel_entity_type", RegistryBuilder::new);

    public static final Supplier<ForgeRegistry<Panel>> PANEL_REGISTRY = Suppliers.memoize(() ->
            RegistryManager.ACTIVE.getRegistry(PANEL_KEY)
    );

    public static final Supplier<ForgeRegistry<PanelEntityType<?>>> PANEL_ENTITY_REGISTRY = Suppliers.memoize(() ->
            RegistryManager.ACTIVE.getRegistry(PANEL_ENTITY_KEY)
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

    public static final PanelEntry<RedstoneIOPanel> REDSTONE_INPUT = ReEngineeredToolbox.getRegistrateAddon()
            .object("redstone_input")
            .panel(() -> new RedstoneIOPanel(RedstoneInputPanelEntity::new))
            .<RedstoneInputPanelEntity>panelEntity(RedstoneInputPanelEntity::new)
            .panelState((context, provider) -> {
                ModelFile inputOn = provider.models().flatPanel("redstone_input_on");
                ModelFile inputOff = provider.models().flatPanel("redstone_input_off");
                provider.directionalPanel(context.get(), panelState -> {
                    if (panelState.getValue(BlockStateProperties.POWERED)) {
                        return inputOn;
                    } else {
                        return inputOff;
                    }
                });
            })
            .item()
            .model((context, provider) -> provider.generated(
                    context,
                    new ResourceLocation(
                            provider.modid(context),
                            "panel/" + provider.name(context) + "_off"
                    )
            ))
            .build()
            .register();

    public static final PanelEntry<RedstoneIOPanel> REDSTONE_OUTPUT = ReEngineeredToolbox.getRegistrateAddon()
            .object("redstone_output")
            .panel(() -> new RedstoneIOPanel(RedstoneInputPanelEntity::new))
            .<RedstoneInputPanelEntity>panelEntity(RedstoneInputPanelEntity::new)
            .panelState((context, provider) -> {
                ModelFile inputOn = provider.models().flatPanel("redstone_output_on");
                ModelFile inputOff = provider.models().flatPanel("redstone_output_off");
                provider.directionalPanel(context.get(), panelState -> {
                    if (panelState.getValue(BlockStateProperties.POWERED)) {
                        return inputOn;
                    } else {
                        return inputOff;
                    }
                });
            })
            .item()
            .model((context, provider) -> provider.generated(
                    context,
                    new ResourceLocation(
                            provider.modid(context),
                            "panel/" + provider.name(context) + "_off"
                    )
            ))
            .build()
            .register();



    public static ForgeRegistry<Panel> getRegistry() {
        return PANEL_REGISTRY.get();
    }

    public static ForgeRegistry<PanelEntityType<?>> getPanelEntityRegistry() {
        return PANEL_ENTITY_REGISTRY.get();
    }

    public static void setup() {

    }
}
