package xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.io.IOException;
import java.io.Reader;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PanelModelBakery {
    private static final PanelModelBakery INSTANCE = new PanelModelBakery();
    private final Map<Panel, PanelStateDefinition> panelPanelStateDefinitionMap;

    public PanelModelBakery() {
        this.panelPanelStateDefinitionMap = new IdentityHashMap<>();
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

    public Set<ResourceLocation> getPanelModelsForLoading() {
        this.loadPanelStates();
        return panelPanelStateDefinitionMap.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .flatMap(panelStateDefinition -> panelStateDefinition.getVariants()
                        .values()
                        .parallelStream()
                        .flatMap(multiVariant -> multiVariant.getVariants()
                                .stream()
                        )
                        .map(Variant::getModelLocation)
                )
                .collect(Collectors.toSet());
    }

    public void clear() {
        this.panelPanelStateDefinitionMap.clear();
    }

    public static PanelModelBakery getInstance() {
        return INSTANCE;
    }
}
