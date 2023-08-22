package xyz.brassgoggledcoders.reengineeredtoolbox.api.data;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.IGeneratedBlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.IStatePropertyPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class PanelStateProvider implements DataProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    private static final Logger LOGGER = LogManager.getLogger();


    protected final Map<Panel, IGeneratedBlockState> registeredPanels = new LinkedHashMap<>();

    private final DataGenerator generator;
    private final String modid;
    private final PanelModelProvider models;

    protected PanelStateProvider(DataGenerator generator, String modid, ExistingFileHelper exFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.models = new PanelModelProvider(generator, modid, exFileHelper) {
            @Override
            public void run(CachedOutput pOutput) {

            }

            @Override
            protected void registerModels() {
            }
        };
    }

    protected abstract void registerStatesAndModels();

    @Override
    public void run(@NotNull CachedOutput cache) {
        models.clear();
        registeredPanels.clear();
        registerStatesAndModels();
        models.generateAll(cache);
        for (Map.Entry<Panel, IGeneratedBlockState> entry : registeredPanels.entrySet()) {
            saveBlockState(cache, entry.getValue().toJson(), entry.getKey());
        }
    }

    private void saveBlockState(CachedOutput cache, JsonObject stateJson, Panel owner) {
        ResourceLocation blockName = Preconditions.checkNotNull(key(owner));
        Path mainOutput = generator.getOutputFolder();
        String pathSuffix = "assets/" + blockName.getNamespace() + "/panelstates/" + blockName.getPath() + ".json";
        Path outputPath = mainOutput.resolve(pathSuffix);
        try {
            DataProvider.saveStable(cache, stateJson, outputPath);
        } catch (IOException e) {
            LOGGER.error("Couldn't save panelstate to {}", outputPath, e);
        }
    }

    private ResourceLocation key(Panel block) {
        return ReEngineeredPanels.getRegistry().getKey(block);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation("minecraft", name);
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(modid, name);
    }

    public ResourceLocation retLoc(String name) {
        return new ResourceLocation("reengineered_toolbox", name);
    }

    @NotNull
    @Override
    public String getName() {
        return "Block States: " + modid;
    }

    public VariantPanelStateBuilder getVariantBuilder(Panel panel) {
        if (registeredPanels.containsKey(panel)) {
            IGeneratedBlockState old = registeredPanels.get(panel);
            Preconditions.checkState(old instanceof VariantPanelStateBuilder);
            return (VariantPanelStateBuilder) old;
        } else {
            VariantPanelStateBuilder ret = new VariantPanelStateBuilder(panel);
            registeredPanels.put(panel, ret);
            return ret;
        }
    }

    public PanelModelProvider models() {
        return this.models;
    }

    public void flatPanel(Panel panel) {
        panel(
                panel,
                this.models()
                        .flatPanel(Objects.requireNonNull(key(panel)).getPath())
        );
    }

    public void openPanel(Panel panel) {
        panel(
                panel,
                this.models()
                        .openPanel(Objects.requireNonNull(key(panel)).getPath())
        );
    }

    public void singleDirectionPanel(Panel panel, Direction dir, ModelFile modelFile) {
        singleDirectionPanel(panel, dir, panelState -> modelFile);
    }

    public void singleDirectionPanel(Panel panel, Direction dir, Function<PanelState, ModelFile> modelFile) {
        getVariantBuilder(panel)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFile.apply(state))
                        .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                        .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + DEFAULT_ANGLE_OFFSET) % 360)
                        .build());
    }

    public void panel(Panel panel, ModelFile modelFile) {
        this.panel(panel, $ -> modelFile);
    }

    public void panel(Panel panel, Function<PanelState, ModelFile> modelFunc) {
        getVariantBuilder(panel)
                .forAllStates(panelState -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(panelState))
                        .build()
                );
    }

    public void directionalPanel(Panel panel, ModelFile model) {
        directionalPanel(panel, model, DEFAULT_ANGLE_OFFSET);
    }

    public void directionalPanel(Panel panel, ModelFile model, int angleOffset) {
        directionalPanel(panel, $ -> model, angleOffset);
    }

    public void directionalPanel(Panel panel, Function<PanelState, ModelFile> modelFunc) {
        directionalPanel(panel, modelFunc, DEFAULT_ANGLE_OFFSET);
    }

    @SuppressWarnings("unchecked")
    public void directionalPanel(Panel panel, Function<PanelState, ModelFile> modelFunc, int angleOffset) {
        Property<Direction> directionProp = Objects.requireNonNull(panel.getComponents(IStatePropertyPanelComponent.class))
                .stream()
                .filter(component -> component.getProperty().getValueClass() == Direction.class)
                .map(component -> ((Property<Direction>) component.getProperty()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("directionalPanel called for Panel with no Direction Property"));

        getVariantBuilder(panel)
                .forAllStates(state -> {
                    Direction dir = state.getValue(Objects.requireNonNull(directionProp));
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });
    }
}
