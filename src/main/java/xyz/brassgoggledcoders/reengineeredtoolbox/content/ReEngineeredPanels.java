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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelEntityPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.interaction.MenuInteractionPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement.RestrictedDirectionPlacementPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.redstone.RedstonePanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.FacingPropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.PanelStatePropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelcomponent.RedstoneSignalPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.FluidIOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.energy.EnergyIOPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item.ItemInputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item.ItemOutputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.DaylightDetectorPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneInputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone.RedstoneOutputPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine.FreezerPanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine.MilkerPanelEntity;
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
            .panel()
            .component(new FacingPropertyComponent())
            .panelState((context, provider) -> provider.openDirectionalPanel(context.get()))
            .item()
            .properties(properties -> properties.tab(null))
            .build()
            .register();

    public static final PanelEntry<Panel> BLANK = ReEngineeredToolbox.getRegistrateAddon()
            .object("blank")
            .panel()
            .component(new FacingPropertyComponent())
            .item()
            .build()
            .register();

    public static final PanelEntry<Panel> ITEM_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("item_input")
                    .panel()
                    .component(new PanelEntityPanelComponent(ItemInputPanelEntity::new))
                    .component(new FacingPropertyComponent())
                    .panelEntity(ItemInputPanelEntity::new)
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<Panel> ITEM_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("item_output")
                    .panel()
                    .component(new PanelEntityPanelComponent(ItemOutputPanelEntity::new))
                    .component(new FacingPropertyComponent())
                    .panelEntity(ItemOutputPanelEntity::new)
                    .item()
                    .build()
                    .register();

    public static final PanelEntry<Panel> FLUID_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("fluid_input")
                    .panel()
                    .component(new PanelEntityPanelComponent(FluidIOPanelEntity.fluidInput()))
                    .component(new FacingPropertyComponent())
                    .panelEntity(FluidIOPanelEntity.fluidInputFactory())
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<Panel> FLUID_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("fluid_output")
                    .panel()
                    .component(new PanelEntityPanelComponent(FluidIOPanelEntity.fluidOutput()))
                    .component(new FacingPropertyComponent())
                    .panelEntity(FluidIOPanelEntity.fluidOutputFactory())
                    .item()
                    .build()
                    .register();

    public static final PanelEntry<Panel> ENERGY_INPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("energy_input")
                    .panel()
                    .component(new PanelEntityPanelComponent(EnergyIOPanelEntity.energyInput()))
                    .component(new FacingPropertyComponent())
                    .panelEntity(EnergyIOPanelEntity.energyInputFactory())
                    .item()
                    .build()
                    .register();


    public static final PanelEntry<Panel> ENERGY_OUTPUT =
            ReEngineeredToolbox.getRegistrateAddon()
                    .object("energy_output")
                    .panel()
                    .component(new PanelEntityPanelComponent(EnergyIOPanelEntity.energyOutput()))
                    .component(new FacingPropertyComponent())
                    .panelEntity(EnergyIOPanelEntity.energyOutputFactory())
                    .item()
                    .build()
                    .register();

    public static final PanelEntry<Panel> REDSTONE_INPUT = ReEngineeredToolbox.getRegistrateAddon()
            .object("redstone_input")
            .panel()
            .component(new RedstonePanelComponent())
            .component(new PanelEntityPanelComponent(RedstoneInputPanelEntity::new))
            .component(new PanelStatePropertyComponent<>(BlockStateProperties.POWERED, false))
            .component(new FacingPropertyComponent())
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

    public static final PanelEntry<Panel> REDSTONE_OUTPUT = ReEngineeredToolbox.getRegistrateAddon()
            .object("redstone_output")
            .panel()
            .component(new RedstoneSignalPanelComponent())
            .component(new PanelEntityPanelComponent(RedstoneInputPanelEntity::new))
            .component(new PanelStatePropertyComponent<>(BlockStateProperties.POWERED, false))
            .component(new FacingPropertyComponent())
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


    public static final PanelEntry<Panel> DISPENSER = ReEngineeredToolbox.getRegistrateAddon()
            .object("dispenser")
            .panel()
            .component(new FacingPropertyComponent())
            .component(new PanelStatePropertyComponent<>(BlockStateProperties.TRIGGERED, false))
            .component(new PanelEntityPanelComponent(DispenserPanelEntity::new))
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

    public static final PanelEntry<Panel> DAYLIGHT_DETECTOR = ReEngineeredToolbox.getRegistrateAddon()
            .object("daylight_detector")
            .panel()
            .component(new RestrictedDirectionPlacementPanelComponent(Direction.UP))
            .component(new PanelEntityPanelComponent(DaylightDetectorPanelEntity::new))
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

    public static final PanelEntry<Panel> FREEZER = ReEngineeredToolbox.getRegistrateAddon()
            .object("freezer")
            .panel()
            .component(new FacingPropertyComponent())
            .component(new PanelEntityPanelComponent(FreezerPanelEntity::new))
            .component(new MenuInteractionPanelComponent())
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

    public static final PanelEntry<Panel> MILKER = ReEngineeredToolbox.getRegistrateAddon()
            .object("milker")
            .panel()
            .component(new PanelStatePropertyComponent<>(BlockStateProperties.TRIGGERED, false))
            .panelEntity(MilkerPanelEntity::new)
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
