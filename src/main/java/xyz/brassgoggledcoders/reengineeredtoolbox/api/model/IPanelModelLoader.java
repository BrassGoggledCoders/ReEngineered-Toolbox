package xyz.brassgoggledcoders.reengineeredtoolbox.api.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;

public interface IPanelModelLoader<T extends IPanelModel> {
    default void onResourceManagerReload(IResourceManager resourceManager) {
        o
    }

    T read(JsonDeserializationContext deserializationContext, JsonObject modelContents);
}
