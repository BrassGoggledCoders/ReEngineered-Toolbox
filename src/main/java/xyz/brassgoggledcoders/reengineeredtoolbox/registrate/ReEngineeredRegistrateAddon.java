package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import java.util.function.Supplier;

public class ReEngineeredRegistrateAddon<S extends AbstractRegistrate<S>> {
    private final S registrate;

    private ReEngineeredRegistrateAddon(S registrate) {
        this.registrate = registrate;
    }

    public <P extends Panel> PanelBuilder<P, ReEngineeredRegistrateAddon<S>> panel(Supplier<P> panelConstructor) {
        return registrate.entry((name, builderCallback) -> new PanelBuilder<>(
                this.registrate,
                this,
                name,
                builderCallback,
                panelConstructor
        ));
    }

    public ReEngineeredRegistrateAddon<S> object(String name) {
        this.registrate.object(name);
        return this;
    }

    public static <S extends AbstractRegistrate<S>> ReEngineeredRegistrateAddon<S> of(S registrate) {
        return new ReEngineeredRegistrateAddon<>(registrate);
    }

}
