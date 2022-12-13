package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public Component getDescription() {
        return this.asPanel().getName();
    }

    @Override
    @NotNull
    public Component getName(@NotNull ItemStack pStack) {
        return this.asPanel().getName();
    }

    @Override
    @NotNull
    public Panel asPanel() {
        return panelSupplier.get();
    }
}
