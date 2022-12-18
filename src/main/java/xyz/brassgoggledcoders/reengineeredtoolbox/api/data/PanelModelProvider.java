package xyz.brassgoggledcoders.reengineeredtoolbox.api.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredToolboxAPI;

public abstract class PanelModelProvider extends ModelProvider<PanelModelBuilder> {

    public PanelModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, "panel", PanelModelBuilder::new, existingFileHelper);
    }

    public PanelModelBuilder flatPanel(String name) {
        return this.withExistingParent(name, new ResourceLocation(ReEngineeredToolboxAPI.ID, "panel/flat_panel"))
                .texture("panel", new ResourceLocation(this.modid, "panel/" + name));
    }

    public PanelModelBuilder openPanel(String name) {
        return this.withExistingParent(name, new ResourceLocation(ReEngineeredToolboxAPI.ID, "panel/open_panel"))
                .texture("panel", new ResourceLocation(this.modid, "panel/" + name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Panel Models: " + modid;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void generateAll(CachedOutput cachedOutput) {
        super.generateAll(cachedOutput);
    }
}
