package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModelRegistry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate.PanelStateDefinition;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.panelstate.PanelStateVariantList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class PanelModelRegistry implements IPanelModelRegistry, ISelectiveResourceReloadListener {
    public static final PanelModelRegistry INSTANCE = new PanelModelRegistry();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(PanelStateDefinition.class, new PanelStateDefinition.Deserializer())
            .registerTypeAdapter(PanelStateVariantList.class, new PanelStateVariantList.Deserializer())
            .create();

    private final Map<ResourceLocation, IPanelModelLoader<?>> panelModelLoaders;
    private final Map<PanelState, IPanelModel> modelsByState;
    private final Map<ResourceLocation, IPanelModel> modelsByName;

    private IResourceManager resourceManager;
    private boolean modelsLoaded = false;

    private volatile boolean registryFrozen = false;

    public PanelModelRegistry() {
        this.panelModelLoaders = Maps.newHashMap();
        this.modelsByState = Maps.newHashMap();
        this.modelsByName = Maps.newHashMap();
        this.resourceManager = Minecraft.getInstance().getResourceManager();
    }

    /**
     * Makes system aware of your loader.
     * <b>Must be called from within {@link ModelRegistryEvent}</b>
     */
    @Override
    public void registerModelLoader(ResourceLocation name, IPanelModelLoader<?> panelModelLoader) {
        synchronized (panelModelLoaders) {
            if (panelModelLoaders.containsKey(name)) {
                ReEngineeredToolbox.LOGGER.warn("Duplication registration for name {}", name);
            } else if (registryFrozen) {
                throw new IllegalStateException("Can not register model loaders after models have started loading. " +
                        "Please use ModelRegistryEvent to register your loaders.");
            } else {
                panelModelLoaders.put(name, panelModelLoader);
            }
        }
    }

    public void freezeRegistry() {
        if (!this.registryFrozen) {
            this.registryFrozen = true;
        }
    }

    @Nullable
    public IPanelModelLoader<?> getModelLoader(ResourceLocation panelLoader) {
        return panelModelLoaders.get(panelLoader);
    }

    @Override
    public IPanelModel getPanelModel(ResourceLocation resourceLocation) {
        this.checkModels();
        return modelsByName.get(resourceLocation);
    }

    @Nullable
    public IPanelModel getPanelModel(PanelState panelState) {
        this.checkModels();
        return this.modelsByState.get(panelState);
    }

    public Collection<IPanelModel> getPanelsByState() {
        this.checkModels();
        return this.modelsByState.values();
    }

    public void reload(IResourceManager resourceManager) {
        this.modelsByState.clear();
        this.modelsLoaded = false;
        this.resourceManager = resourceManager;
    }

    public void checkModels() {
        if (!this.registryFrozen) {
            ReEngineeredToolbox.LOGGER.warn("Attempted to get panel models before registry frozen");
        } else if (!this.modelsLoaded) {
            RegistryManager.ACTIVE.getRegistry(Panel.class)
                    .getValues()
                    .stream()
                    .map(this::loadModelsFor)
                    .filter(Objects::nonNull)
                    .forEach(modelsByState::putAll);
            this.modelsLoaded = true;
        }
    }

    private Map<PanelState, IPanelModel> loadModelsFor(Panel panel) {
        ResourceLocation panelRegistryName = Objects.requireNonNull(panel.getRegistryName());
        ResourceLocation panelStateLocation = new ResourceLocation(
                panelRegistryName.getNamespace(),
                "panelstates/" + panelRegistryName.getPath() + ".json"
        );
        try {
            IResource panelStateDefinitionResource = resourceManager.getResource(panelStateLocation);
            PanelStateDefinition panelStateDefinition = JSONUtils.fromJson(
                    GSON,
                    new InputStreamReader(panelStateDefinitionResource.getInputStream()),
                    PanelStateDefinition.class
            );
        } catch (IOException e) {
            ReEngineeredToolbox.LOGGER.warn("Failed to load panel state definition for " + panelStateLocation, e);
        }

        return null;
    }

    private IPanelModel getMissingModel() {
        return new PanelModel(Maps.newHashMap(), Lists.newArrayList());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.reload(resourceManager);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.MODELS)) {
            this.reload(resourceManager);
        }
    }

    @Nullable
    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }
}
