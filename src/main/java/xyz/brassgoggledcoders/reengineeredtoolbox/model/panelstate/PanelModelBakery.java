package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.io.IOException;
import java.io.Reader;
import java.util.IdentityHashMap;
import java.util.Map;

public class PanelModelBakery {
    private static final PanelModelBakery INSTANCE = new PanelModelBakery();
    private final Map<Panel, PanelStateDefinition> panelPanelStateDefinitionMap;
    private final Table<PanelState, Direction, BakedModel> panelStateBakedModelLoadingCache;

    public PanelModelBakery() {
        this.panelPanelStateDefinitionMap = new IdentityHashMap<>();
        this.panelStateBakedModelLoadingCache = HashBasedTable.create();
    }

    @NotNull
    public BakedModel getPanelStateModel(@NotNull PanelState key, @NotNull Direction direction) {
        BakedModel bakedModel = this.panelStateBakedModelLoadingCache.get(key, direction);
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
                        for (Direction direction : Direction.values()) {
                            if (panelState.isValidFor(direction)) {
                                MultiVariant multiVariant = definition.getVariant(BlockModelShaper.statePropertiesToString(panelState.getValues()));
                                if (multiVariant != null) {
                                    WeightedBakedModel.Builder builder = new WeightedBakedModel.Builder();

                                    for (Variant variant : multiVariant.getVariants()) {
                                        BakedModel bakedmodel = modelBakery.bake(
                                                variant.getModelLocation(),
                                                this.getModelTransform(direction),
                                                modelBakery.getAtlasSet()::getSprite
                                        );
                                        builder.add(bakedmodel, variant.getWeight());
                                    }

                                    BakedModel bakedModel = builder.build();
                                    if (bakedModel != null) {
                                        this.panelStateBakedModelLoadingCache.put(
                                                panelState,
                                                direction,
                                                bakedModel
                                        );
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private ModelState getModelTransform(Direction direction) {
        return switch (direction) {
            case EAST:
                yield BlockModelRotation.X0_Y90;
            case WEST:
                yield BlockModelRotation.X0_Y270;
            case DOWN:
                yield BlockModelRotation.X90_Y0;
            case UP:
                yield BlockModelRotation.X270_Y0;
            case SOUTH:
                yield BlockModelRotation.X0_Y180;
            default:
                yield BlockModelRotation.X0_Y0;
        };
    }

    public static PanelModelBakery getInstance() {
        return INSTANCE;
    }
}
