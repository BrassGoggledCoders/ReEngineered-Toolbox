package xyz.brassgoggledcoders.reengineeredtoolbox.api.data.panel;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class PanelStateProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final Map<Panel, IGeneratedPanelState> registeredPanels = new LinkedHashMap<>();
    private final DataGenerator generator;
    private final PanelModelProvider panelModels;

    public PanelStateProvider(DataGenerator gen, String id, ExistingFileHelper exFileHelper) {
        this.generator = gen;
        this.panelModels = new PanelModelProvider(generator, id, exFileHelper);
    }

    @Override
    public void run(@Nonnull DirectoryCache cache) {
        models().clear();
        registeredPanels.clear();
        registerStatesAndModels();
        models().generateAll(cache);
        for (Map.Entry<Panel, IGeneratedPanelState> entry : registeredPanels.entrySet()) {
            savePanelState(cache, entry.getValue().get(), entry.getKey());
        }
    }

    private void savePanelState(DirectoryCache cache, JsonElement stateJson, Panel owner) {
        ResourceLocation blockName = Preconditions.checkNotNull(owner.getRegistryName());
        Path mainOutput = generator.getOutputFolder();
        String pathSuffix = "assets/" + blockName.getNamespace() + "/panelstates/" + blockName.getPath() + ".json";
        Path outputPath = mainOutput.resolve(pathSuffix);
        try {
            IDataProvider.save(GSON, cache, stateJson, outputPath);
        } catch (IOException e) {
            LOGGER.error("Couldn't save panelstate to {}", outputPath, e);
        }
    }

    public PanelModelProvider models() {
        return panelModels;
    }

    protected abstract void registerStatesAndModels();

    public void simplePanel(Panel panel) {
        simplePanel(panel, models().flatPanel(name(panel), texture(panel)));
    }

    public void simplePanel(Panel panel, ModelFile model) {
        simplePanel(panel, new ConfiguredModel(model));
    }

    public void simplePanel(Panel panel, ConfiguredModel... models) {
        getVariantBuilder(panel)
                .partialState()
                .setModels(models);
    }

    public VariantPanelStateBuilder getVariantBuilder(Panel panel) {
        if (registeredPanels.containsKey(panel)) {
            IGeneratedPanelState old = registeredPanels.get(panel);
            Preconditions.checkState(old instanceof VariantPanelStateBuilder);
            return (VariantPanelStateBuilder) old;
        } else {
            VariantPanelStateBuilder ret = new VariantPanelStateBuilder(panel);
            registeredPanels.put(panel, ret);
            return ret;
        }
    }

    private String name(Panel panel) {
        return Objects.requireNonNull(panel.getRegistryName()).getPath();
    }

    public ResourceLocation texture(Panel panel) {
        ResourceLocation name = Objects.requireNonNull(panel.getRegistryName());
        return new ResourceLocation(name.getNamespace(), PanelModelProvider.FOLDER + "/" + name.getPath());
    }
}
