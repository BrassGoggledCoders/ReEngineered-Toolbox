package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.data.PanelStateProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import java.util.Optional;

public class RegistratePanelStateProvider extends PanelStateProvider implements RegistrateProvider {
    public static final ProviderType<RegistratePanelStateProvider> PANEL_STATE_TYPE = ProviderType.register(
            "panelstate",
            (registrate, dataEvent) -> new RegistratePanelStateProvider(registrate, dataEvent.getGenerator(), dataEvent.getExistingFileHelper())
    );

    private final AbstractRegistrate<?> parent;

    public RegistratePanelStateProvider(AbstractRegistrate<?> parent, DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, parent.getModid(), exFileHelper);
        this.parent = parent;
    }

    @Override
    @NotNull
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    protected void registerStatesAndModels() {
        parent.genData(PANEL_STATE_TYPE, this);
    }

    @Override
    @NotNull
    public String getName() {
        return "PanelStates";
    }

    @SuppressWarnings("null")
    public Optional<VariantBlockStateBuilder> getExistingVariantBuilder(Panel panel) {
        return Optional.ofNullable(registeredPanels.get(panel))
                .filter(b -> b instanceof VariantBlockStateBuilder)
                .map(b -> (VariantBlockStateBuilder) b);
    }
}
