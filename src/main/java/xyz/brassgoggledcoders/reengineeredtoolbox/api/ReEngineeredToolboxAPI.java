package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModelRegistry;

import java.util.Objects;

public class ReEngineeredToolboxAPI {
    private static IPanelModelRegistry panelModelRegistry;

    public static IPanelModelRegistry getPanelModelRegistry() {
        return Objects.requireNonNull(panelModelRegistry, "Called for Panel Model Registry before it was created, or on server");
    }

    public static void setPanelModelRegistry(IPanelModelRegistry panelModelRegistry) {
        if (ReEngineeredToolboxAPI.panelModelRegistry != null) {
            throw new IllegalStateException("Panel Model Registry set multiple times");
        } else {
            ReEngineeredToolboxAPI.panelModelRegistry = panelModelRegistry;
        }
    }
}
