package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.model.SimpleModelState;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.io.IOException;
import java.io.Reader;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PanelModelBakery {
    private static final PanelModelBakery INSTANCE = new PanelModelBakery();

    private static final SimpleModelState IDENTITY = new SimpleModelState(Transformation.identity());
    private static final ResourceLocation UNUSED = new ResourceLocation("unused");
    private final Map<Panel, PanelStateDefinition> panelPanelStateDefinitionMap;
    private final Map<PanelState, BakedModel> panelStateBakedModelLoadingCache;

    public PanelModelBakery() {
        this.panelPanelStateDefinitionMap = new IdentityHashMap<>();
        this.panelStateBakedModelLoadingCache = new IdentityHashMap<>();
    }

    @NotNull
    public BakedModel getPanelStateModel(@NotNull PanelState key) {
        BakedModel bakedModel = this.panelStateBakedModelLoadingCache.get(key);
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
                            BakedModel bakedModel = multiVariant.bake(
                                    modelBakery,
                                    modelBakery.getAtlasSet()::getSprite,
                                    IDENTITY,
                                    UNUSED
                            );
                            if (bakedModel != null) {
                                this.panelStateBakedModelLoadingCache.put(
                                        panelState,
                                        bakedModel
                                );
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
