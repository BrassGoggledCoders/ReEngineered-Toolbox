package xyz.brassgoggledcoders.reengineeredtoolbox.api.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PanelModelBuilder extends ModelBuilder<PanelModelBuilder> {
    public PanelModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }
}
