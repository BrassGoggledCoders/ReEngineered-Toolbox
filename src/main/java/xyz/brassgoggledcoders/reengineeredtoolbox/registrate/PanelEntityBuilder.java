package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

public class PanelEntityBuilder<T extends PanelEntity, P> extends AbstractBuilder<PanelEntityType<?>, PanelEntityType<T>, P, PanelEntityBuilder<T, P>> {

    public interface PanelEntityFactory<T extends PanelEntity> {
        T create(PanelEntityType<T> type, IFrameEntity frameEntity, PanelState panelState);
    }

    public static <T extends PanelEntity, P> PanelEntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, PanelEntityBuilder.PanelEntityFactory<T> factory) {
        return new PanelEntityBuilder<>(owner, parent, name, callback, factory);
    }

    private final PanelEntityBuilder.PanelEntityFactory<T> factory;

    protected PanelEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, PanelEntityBuilder.PanelEntityFactory<T> factory) {
        super(owner, parent, name, callback, ReEngineeredPanels.PANEL_ENTITY_KEY);
        this.factory = factory;
    }

    @Override
    @NotNull
    protected PanelEntityType<T> createEntry() {
        PanelEntityBuilder.PanelEntityFactory<T> factory = this.factory;
        return new PanelEntityType<>(factory::create);
    }

    @Override
    protected RegistryEntry<PanelEntityType<T>> createEntryWrapper(RegistryObject<PanelEntityType<T>> delegate) {
        return new PanelEntityEntry<>(getOwner(), delegate);
    }

    @Override
    public PanelEntityEntry<T> register() {
        return (PanelEntityEntry<T>) super.register();
    }
}
