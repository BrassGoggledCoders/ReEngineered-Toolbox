package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;

import java.util.Collection;
import java.util.function.Function;

public class ReEngineeredRegistrateAddon<S extends AbstractRegistrate<S>> {
    private final S registrate;

    private ReEngineeredRegistrateAddon(S registrate) {
        this.registrate = registrate;
    }

    public PanelBuilder<Panel, ReEngineeredRegistrateAddon<S>> panel() {
        return this.panel(Panel::new);
    }

    public <P extends Panel> PanelBuilder<P, ReEngineeredRegistrateAddon<S>> panel(Function<Collection<PanelComponent>, P> panelConstructor) {
        return registrate.entry((name, builderCallback) -> PanelBuilder.create(
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
