package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelLike;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.PanelItem;

import java.util.function.Supplier;

public class PanelBuilder<P extends Panel, R> extends AbstractBuilder<Panel, P, R, PanelBuilder<P, R>> {
    private final Supplier<P> panelConstructor;

    public PanelBuilder(AbstractRegistrate<?> owner, R parent, String name, BuilderCallback callback, Supplier<P> panelConstructor) {
        super(owner, parent, name, callback, ReEngineeredPanels.PANEL_KEY);
        this.panelConstructor = panelConstructor;
    }

    public PanelBuilder<P, R> panelState(NonNullBiConsumer<DataGenContext<Panel, P>, RegistratePanelStateProvider> cons) {
        this.setData(RegistratePanelStateProvider.PANEL_STATE_TYPE, cons);
        return this;
    }

    public PanelBuilder<P, R> defaultPanelState() {
        return this.panelState((context, provider) -> provider.flatDirectionalPanel(context.get()));
    }

    public PanelBuilder<P, R> defaultLang() {
        return lang(Panel::getDescriptionId);
    }

    public PanelBuilder<P, R> lang(String name) {
        return lang(Panel::getDescriptionId, name);
    }

    public PanelBuilder<P, R> defaultItem() {
        return item()
                .build();
    }

    public ItemBuilder<PanelItem<P>, PanelBuilder<P, R>> item() {
        return item(PanelItem::new);
    }

    public <T extends Item & PanelLike> ItemBuilder<T, PanelBuilder<P, R>> item(
            NonNullBiFunction<Supplier<P>, Item.Properties, T> constructor
    ) {
        return getOwner().item(this, getName(), p -> constructor.apply(this.get(), p))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .model((context, provider) -> provider.generated(
                        context,
                        new ResourceLocation(
                                provider.modid(context),
                                "panel/" + provider.name(context)
                        )
                ));
    }

    @Override
    @NotNull
    @NonnullType
    protected P createEntry() {
        return panelConstructor.get();
    }

    @Override
    @NotNull
    protected RegistryEntry<P> createEntryWrapper(@NotNull RegistryObject<P> delegate) {
        return new PanelEntry<>(getOwner(), delegate);
    }

    @Override
    @NotNull
    public PanelEntry<P> register() {
        return (PanelEntry<P>) super.register();
    }

    public static <P extends Panel, R> PanelBuilder<P, R> create(AbstractRegistrate<?> owner, R parent, String name, BuilderCallback callback, Supplier<P> factory, Material material) {
        return new PanelBuilder<>(owner, parent, name, callback, factory)
                .defaultPanelState()
                .defaultLang();
    }
}
