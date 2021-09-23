package xyz.brassgoggledcoders.reengineeredtoolbox.api.data.panel;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class PanelModelProvider extends ModelProvider<PanelModelBuilder> {
    public static final String FOLDER = "panel";

    public PanelModelProvider(DataGenerator generator, String id, ExistingFileHelper existingFileHelper) {
        super(generator, id, FOLDER, PanelModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void generateAll(DirectoryCache cache) {
        super.generateAll(cache);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Panel Models: " + modid;
    }

    public ResourceLocation retLoc(String path) {
        return new ResourceLocation("reengineeredtoolbox", path);
    }

    public ModelFile flatPanel(String name, ResourceLocation texture) {
        return withExistingParent(name, retLoc(FOLDER + "/flat_panel"))
                .texture("panel", texture);
    }
}
