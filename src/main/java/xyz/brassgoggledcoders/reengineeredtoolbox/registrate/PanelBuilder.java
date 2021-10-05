package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.item.Item;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.PanelItem;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

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

    public PanelBuilder<T, P> defaultPanelstate() {
        return panelstate((ctx, prov) -> prov.simplePanel(ctx.getEntry()));
    }

    public PanelBuilder<T, P> defaultItem() {
        return item((panelSupplier, properties) -> new PanelItem<>(panelSupplier::get, properties))
                .model((context, provider) -> provider.generated(context, provider.modLoc("panel/" + context.getName())))
                .build();
    }

    public <I extends Item> ItemBuilder<I, PanelBuilder<T, P>> item(BiFunction<NonNullSupplier<T>, Item.Properties, I> creator) {
        return this.getOwner().item(this, properties -> creator.apply(this::getEntry, properties));
    }

    /**
     * Configure the blockstate/models for this block.
     *
     * @param cons The callback which will be invoked during data generation.
     * @return this {@link BlockBuilder}
     * @see #setData(ProviderType, NonNullBiConsumer)
     */
    public PanelBuilder<T, P> panelstate(NonNullBiConsumer<DataGenContext<Panel, T>, RegistratePanelStateProvider> cons) {
        return setData(RegistratePanelStateProvider.PROVIDER_TYPE, cons);
    }

    public PanelBuilder<T, P> defaultLang() {
        return lang(Panel::getDescriptionId);
    }

    @Override
    @Nonnull
    protected @NonnullType T createEntry() {
        return panelCreator.get();
    }

    public static <T2 extends Panel, P2 extends AbstractRegistrate<P2>> NonNullBiFunction<String, BuilderCallback, PanelBuilder<T2, P2>> entry(P2 parent) {
        return (name, builder) -> new PanelBuilder<T2, P2>(parent, parent, name, builder, Panel.class)
                .defaultPanelstate()
                .defaultItem()
                .defaultLang();
    }

    public static <T2 extends Panel> NonNullBiFunction<String, BuilderCallback, PanelBuilder<T2, Registrate>> entry() {
        return PanelBuilder.entry(ReEngineeredToolbox.getRegistrate());
    }
}
