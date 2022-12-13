package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

public class PanelEntry<P extends Panel> extends ItemProviderEntry<P> {
    public PanelEntry(AbstractRegistrate<?> owner, RegistryObject<P> delegate) {
        super(owner, delegate);
    }
}
