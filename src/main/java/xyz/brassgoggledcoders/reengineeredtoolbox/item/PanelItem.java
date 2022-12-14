package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelLike;

import java.util.function.Supplier;

public class PanelItem<P extends Panel> extends Item implements PanelLike {
    private final Supplier<P> panelSupplier;

    public PanelItem(Supplier<P> panelSupplier, Properties pProperties) {
        super(pProperties);
        this.panelSupplier = panelSupplier;
    }

    @Override
    @NotNull
    public String getDescriptionId() {
        return this.asPanel().getDescriptionId();
    }

    @Override
    @NotNull
    public Panel asPanel() {
        return panelSupplier.get();
    }
}
