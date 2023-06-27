package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.PanelWithMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.io.DaylightDetectorPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.io.IOPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.io.RedstoneIOPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.panel.world.DispenserPanel;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.FluidIOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.energy.EnergyIOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item.ItemInputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item.ItemOutputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.DaylightDetectorPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneInputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneOutputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine.FreezerPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser.DispenserPanelEntity;
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

    public static final PanelEntry<Panel> BLANK = ReEngineeredToolbox.getRegistrateAddon()
            .object("blank")
            .panel(Panel::new)
            .item()
            .build()
            .register();

    public static final PanelEntry<IOPanel> ITEM_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("item_input")
                    .panel(() -> new IOPanel(ItemInputPanelEntity::new))
                    .panelEntity(ItemInputPanelEntity::new)
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<IOPanel> ITEM_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("item_output")
                    .panel(() -> new IOPanel(ItemOutputPanelEntity::new))
                    .panelEntity(ItemOutputPanelEntity::new)
                    .item()
                    .build()
                    .register();

    public static final PanelEntry<IOPanel> FLUID_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("fluid_input")
                    .panel(() -> new IOPanel(FluidIOPanelEntity.fluidInput()))
                    .panelEntity(FluidIOPanelEntity.fluidInputFactory())
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<IOPanel> FLUID_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("fluid_output")
                    .panel(() -> new IOPanel(FluidIOPanelEntity.fluidOutput()))
                    .panelEntity(FluidIOPanelEntity.fluidOutputFactory())
                    .item()
                    .build()
                    .register();

    public static final PanelEntry<IOPanel> ENERGY_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("energy_input")
                    .panel(() -> new IOPanel(EnergyIOPanelEntity.energyInput()))
                    .panelEntity(EnergyIOPanelEntity.energyInputFactory())
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<IOPanel> ENERGY_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("energy_output")
                    .panel(() -> new IOPanel(EnergyIOPanelEntity.energyOutput()))
                    .panelEntity(EnergyIOPanelEntity.energyOutputFactory())
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
            .panel(() -> new RedstoneIOPanel(RedstoneOutputPanelEntity::new))
            .<RedstoneOutputPanelEntity>panelEntity(RedstoneOutputPanelEntity::new)
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


    public static final PanelEntry<DispenserPanel> DISPENSER = ReEngineeredToolbox.getRegistrateAddon()
            .object("dispenser")
            .panel(DispenserPanel::new)
            .panelEntity(DispenserPanelEntity::new)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("D")
                    .pattern("P")
                    .define('D', Items.DISPENSER)
                    .define('P', BLANK.asPanel())
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(BLANK.asPanel()))
                    .save(provider)
            )
            .build()
            .register();

    public static final PanelEntry<DaylightDetectorPanel> DAYLIGHT_DETECTOR = ReEngineeredToolbox.getRegistrateAddon()
            .object("daylight_detector")
            .panel(DaylightDetectorPanel::new)
            .panelState((context, provider) -> provider.singleDirectionPanel(
                    context.get(),
                    Direction.UP,
                    provider.models()
                            .flatPanel(context.getName(), provider.mcLoc("block/daylight_detector_top"))
            ))
            .panelEntity(DaylightDetectorPanelEntity::new)
            .item()
            .model((context, provider) -> provider.generated(context, provider.mcLoc("block/daylight_detector_top")))
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("D")
                    .pattern("P")
                    .define('D', Items.DAYLIGHT_DETECTOR)
                    .define('P', BLANK.asPanel())
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(BLANK.asPanel()))
                    .save(provider)
            )
            .build()
            .register();

    public static final PanelEntry<PanelWithMenu<FreezerPanelEntity>> FREEZER = ReEngineeredToolbox.getRegistrateAddon()
            .object("freezer")
            .panel(() -> new PanelWithMenu<>(FreezerPanelEntity::new))
            .panelEntity(FreezerPanelEntity::new)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("IFI")
                    .pattern("IPI")
                    .define('I', Items.PACKED_ICE)
                    .define('F', Items.FURNACE)
                    .define('P', BLANK.asPanel())
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(BLANK.asPanel()))
                    .save(provider)
            )
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
