package xyz.brassgoggledcoders.reengineeredtoolbox.api.model;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import java.util.Collection;

public interface IPanelModelRegistry {
    /**
     * Makes system aware of your loader.
     * <b>Must be called from within {@link ModelRegistryEvent}</b>
     */
    void registerModelLoader(ResourceLocation id, IPanelModelLoader<?> loader);

    IPanelModelLoader<?> getModelLoader(ResourceLocation id);

    IPanelModel getPanelModel(ResourceLocation resourceLocation);

    IPanelModel getPanelModel(PanelState panelState);

    Collection<IPanelModel> getPanelsByState();
}
