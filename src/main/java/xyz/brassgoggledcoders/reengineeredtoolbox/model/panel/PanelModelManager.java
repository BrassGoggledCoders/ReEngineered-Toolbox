package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PanelModelManager implements ISelectiveResourceReloadListener {
    public static final PanelModelManager INSTANCE = new PanelModelManager();
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BlockModelDefinition.class, new BlockModelDefinition.Deserializer())
            .registerTypeAdapter(Variant.class, new Variant.Deserializer())
            .registerTypeAdapter(VariantList.class, new TransformedVariantList.Deserializer())
            .create();

    private final Map<ResourceLocation, IUnbakedModel> unbakedModels;
    private final Set<RenderMaterial> renderMaterials;
    private final PanelModelShapes panelModelShapes;

    private IResourceManager resourceManager;

    public PanelModelManager() {
        this.unbakedModels = Maps.newHashMap();
        this.renderMaterials = Sets.newHashSet();
        this.resourceManager = Minecraft.getInstance()
                .getResourceManager();
        this.panelModelShapes = new PanelModelShapes(this);
    }

    public Collection<RenderMaterial> getMaterials(Function<ResourceLocation, IUnbakedModel> modelGetter,
                                                   Set<Pair<String, String>> pMissingTextureErrors) {
        if (renderMaterials.isEmpty()) {
            this.renderMaterials.addAll(this.getUnbakedModels()
                    .stream()
                    .flatMap(unbakedModel -> unbakedModel.getMaterials(modelGetter, pMissingTextureErrors)
                            .stream()
                    )
                    .collect(Collectors.toSet())
            );
        }
        return renderMaterials;
    }

    public Collection<IUnbakedModel> getUnbakedModels() {
        if (this.unbakedModels.isEmpty()) {
            this.loadUnbakedModels();
        }
        return this.unbakedModels.values();
    }

    private void loadUnbakedModels() {
        for (Panel panel : RETRegistries.PANELS.get()) {
            this.loadUnbakedModelFor(panel);
        }
    }

    private void loadUnbakedModelFor(Panel panel) {
        try {
            ResourceLocation registryName = Objects.requireNonNull(panel.getRegistryName());
            ResourceLocation panelStateLocation = new ResourceLocation(
                    registryName.getNamespace(),
                    "panelstate/" + registryName.getPath() + ".json"
            );
            IResource panelStateResource = resourceManager.getResource(panelStateLocation);
            BlockModelDefinition panelModelDefinition = JSONUtils.fromJson(
                    GSON,
                    new InputStreamReader(panelStateResource.getInputStream()),
                    BlockModelDefinition.class
            );
            if (panelModelDefinition == null) {
                throw new JsonParseException("Failed to parse PanelState Definition for " + panelStateLocation);
            }
            panel.getStateContainer()
                    .getPossibleStates()
                    .forEach(panelState -> {
                        ResourceLocation name = PanelModelShapes.stateToModelLocation(panelState);
                        String properties = BlockModelShapes.statePropertiesToString(panelState.getValues());
                        IUnbakedModel model = panelModelDefinition.getVariants()
                                .get(properties);
                        if (model == null) {
                            ReEngineeredToolbox.LOGGER.warn("Failed to find PanelState Definition for " + properties +
                                    " in " + panelStateLocation);
                        } else {
                            unbakedModels.put(name, model);
                        }
                    });
        } catch (IOException | JsonParseException exception) {
            ReEngineeredToolbox.LOGGER.warn("Failed load Models for Panel " + panel.getRegistryName(), exception);
        }
    }

    public IUnbakedModel getUnbakedModel(ResourceLocation resourceLocation) {
        if (unbakedModels.isEmpty()) {
            this.loadUnbakedModels();
        }
        return unbakedModels.get(resourceLocation);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(this.getResourceType())) {
            this.unbakedModels.clear();
            this.renderMaterials.clear();
            this.resourceManager = resourceManager;
            this.panelModelShapes.clearCaches();
        }
    }

    @Nullable
    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    public PanelModelShapes getShapes() {
        return this.panelModelShapes;
    }
}
