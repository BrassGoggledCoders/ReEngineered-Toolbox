package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.Direction;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelLike;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

public class PanelEntry<P extends Panel> extends ItemProviderEntry<P> implements PanelLike {
    public PanelEntry(AbstractRegistrate<?> owner, RegistryObject<P> delegate) {
        super(owner, delegate);
    }

    @Override
    @NotNull
    public Panel asPanel() {
        return this.get();
    }

    public PanelState getDefaultState() {
        return this.asPanel().defaultPanelState();
    }

    public PanelState withDirection(Direction direction) {
        return this.getDefaultState().withDirection(direction);
    }
}
