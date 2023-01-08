package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.Direction;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelLike;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

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

    public PanelEntityEntry<?> getPanelEntry() {
        return PanelEntityEntry.cast(this.getSibling(ReEngineeredPanels.PANEL_ENTITY_KEY));
    }

    public PanelEntityType<?> getPanelEntityType() {
        return this.getPanelEntry().get();
    }

    @Nullable
    public PanelEntity createPanelEntity(IFrameEntity frame, PanelState panelState) {
        return this.getPanelEntry()
                .create(frame, panelState);
    }
}
