package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.LogicalSide;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.data.panel.PanelStateProvider;

import javax.annotation.Nonnull;

public class RegistratePanelStateProvider extends PanelStateProvider implements RegistrateProvider {
    public static final ProviderType<RegistratePanelStateProvider> PROVIDER_TYPE = ProviderType.register(
            "panelstate",
            (registrate, event) -> new RegistratePanelStateProvider(registrate, event.getGenerator(), event.getExistingFileHelper())
    );

    private final AbstractRegistrate<?> parent;

    public RegistratePanelStateProvider(AbstractRegistrate<?> parent, DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, parent.getModid(), exFileHelper);
        this.parent = parent;
    }

    @Override
    @Nonnull
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    protected void registerStatesAndModels() {
        this.parent.genData(PROVIDER_TYPE, this);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Panelstates";
    }
}
