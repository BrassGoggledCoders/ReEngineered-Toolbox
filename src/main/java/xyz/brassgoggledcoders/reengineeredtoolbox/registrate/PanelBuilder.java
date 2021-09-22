package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import javax.annotation.Nonnull;

public class PanelBuilder<T extends Panel, P> extends AbstractBuilder<Panel, T, P, PanelBuilder<T, P>> {
    private NonNullSupplier<T> panelCreator;

    public PanelBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                        Class<? super Panel> registryType) {
        super(owner, parent, name, callback, registryType);
    }

    public PanelBuilder<T, P> panel(NonNullSupplier<T> panelCreator) {
        this.panelCreator = panelCreator;
        return this;
    }

    @Override
    @Nonnull
    protected @NonnullType T createEntry() {
        return panelCreator.get();
    }

    public static <T2 extends Panel, P2 extends AbstractRegistrate<P2>> NonNullBiFunction<String, BuilderCallback, PanelBuilder<T2, P2>> entry(P2 parent) {
        return (name, builder) -> new PanelBuilder<>(parent, parent, name, builder, Panel.class);
    }

    public static <T2 extends Panel> NonNullBiFunction<String, BuilderCallback, PanelBuilder<T2, Registrate>> entry() {
        return PanelBuilder.entry(ReEngineeredToolbox.getRegistrate());
    }
}
