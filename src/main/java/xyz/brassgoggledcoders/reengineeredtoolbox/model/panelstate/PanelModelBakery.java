package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.math.Transformation;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.model.SimpleModelState;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement.IPlacementRequirementPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.io.IOException;
import java.io.Reader;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PanelModelBakery {
    private static final PanelModelBakery INSTANCE = new PanelModelBakery();

    private static final SimpleModelState IDENTITY = new SimpleModelState(Transformation.identity());
    private static final EnumMap<Direction, ModelState> DIRECTION_STATE = Util.make(
            new EnumMap<>(Direction.class),
            map -> {
                map.put(Direction.UP, BlockModelRotation.X0_Y0);
                map.put(Direction.DOWN, BlockModelRotation.X180_Y0);
                map.put(Direction.NORTH, BlockModelRotation.X90_Y0);
                map.put(Direction.SOUTH, BlockModelRotation.X90_Y180);
                map.put(Direction.WEST, BlockModelRotation.X90_Y270);
                map.put(Direction.EAST, BlockModelRotation.X90_Y90);
            }
    );

    private static final ResourceLocation UNUSED = new ResourceLocation("unused");
    private final Map<Panel, PanelStateDefinition> panelPanelStateDefinitionMap;
    private final Table<PanelState, Direction, BakedModel> panelStateBakedModelLoadingCache;

    public PanelModelBakery() {
        this.panelPanelStateDefinitionMap = new IdentityHashMap<>();
        this.panelStateBakedModelLoadingCache = HashBasedTable.create();
    }

    @NotNull
    public BakedModel getPanelStateModel(@NotNull PanelState key, @NotNull Direction facing) {
        BakedModel bakedModel = this.panelStateBakedModelLoadingCache.get(key, facing);
        return bakedModel != null ? bakedModel : Minecraft.getInstance().getModelManager().getMissingModel();
    }

    private void loadPanelStates() {
        if (panelPanelStateDefinitionMap.isEmpty()) {
            for (Map.Entry<ResourceKey<Panel>, Panel> entry : ReEngineeredPanels.getRegistry().getEntries()) {
                ResourceLocation definitionLocation = new ResourceLocation(
                        entry.getKey().location().getNamespace(),
                        "panelstates/" + entry.getKey().location().getPath() + ".json"
                );
                for (Resource resource : Minecraft.getInstance().getResourceManager().getResourceStack(definitionLocation)) {
                    try (Reader reader = resource.openAsReader()) {
                        panelPanelStateDefinitionMap.put(entry.getValue(), PanelStateDefinition.fromStream(reader));
                    } catch (IOException e) {
                        ReEngineeredToolbox.LOGGER.warn(
                                "Exception loading panelstate definition: '{}' in resource pack: '{}': {}",
                                definitionLocation,
                                resource.sourcePackId(),
                                e.getMessage()
                        );
                    }
                }
            }
        }
    }

    public void clear() {
        this.panelPanelStateDefinitionMap.clear();
    }

    public void bake(ModelBakery modelBakery) {
        loadPanelStates();
        for (Map.Entry<Panel, PanelStateDefinition> definitionEntry : panelPanelStateDefinitionMap.entrySet()) {
            PanelStateDefinition definition = definitionEntry.getValue();
            definitionEntry.getKey()
                    .getStateDefinition()
                    .getPossibleStates()
                    .forEach(panelState -> {
                        MultiVariant multiVariant = definition.getVariant(BlockModelShaper.statePropertiesToString(panelState.getValues()));
                        if (multiVariant != null) {
                            if (panelState.getFacingProperty() != null) {
                                BakedModel bakedModel = multiVariant.bake(
                                        modelBakery,
                                        modelBakery.getAtlasSet()::getSprite,
                                        IDENTITY,
                                        UNUSED
                                );
                                if (bakedModel != null) {
                                    this.panelStateBakedModelLoadingCache.put(
                                            panelState,
                                            panelState.getValue(panelState.getFacingProperty()),
                                            bakedModel
                                    );
                                }
                            } else {
                                Predicate<Direction> validDirection = direction -> true;
                                IPlacementRequirementPanelComponent requirementPanel = panelState.getPanel()
                                        .getComponent(IPlacementRequirementPanelComponent.class);
                                if (requirementPanel != null) {
                                    validDirection = requirementPanel::isValidDirection;
                                }
                                for (Map.Entry<Direction, ModelState> directionState : DIRECTION_STATE.entrySet()) {
                                    if (validDirection.test(directionState.getKey())) {
                                        BakedModel bakedModel = multiVariant.bake(
                                                modelBakery,
                                                modelBakery.getAtlasSet()::getSprite,
                                                directionState.getValue(),
                                                UNUSED
                                        );
                                        if (bakedModel != null) {
                                            this.panelStateBakedModelLoadingCache.put(
                                                    panelState,
                                                    directionState.getKey(),
                                                    bakedModel
                                            );
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void registerPanelModels(Consumer<ResourceLocation> register) {
        loadPanelStates();
        panelPanelStateDefinitionMap.values()
                .stream()
                .flatMap(panelStateDefinition -> panelStateDefinition.getVariants()
                        .values()
                        .stream()
                )
                .flatMap(multiVariant -> multiVariant.getVariants()
                        .stream()
                        .map(Variant::getModelLocation)
                )
                .forEach(register);
    }

    public static PanelModelBakery getInstance() {
        return INSTANCE;
    }
}
