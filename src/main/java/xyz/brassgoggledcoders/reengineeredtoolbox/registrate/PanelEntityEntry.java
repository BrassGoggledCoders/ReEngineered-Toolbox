package xyz.brassgoggledcoders.reengineeredtoolbox.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.IPanelPosition;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;

import javax.annotation.Nullable;
import java.util.Optional;

public class PanelEntityEntry<T extends PanelEntity> extends RegistryEntry<PanelEntityType<T>> {

    public PanelEntityEntry(AbstractRegistrate<?> owner, RegistryObject<PanelEntityType<T>> delegate) {
        super(owner, delegate);
    }

    public T create(IFrameEntity pos, PanelState state) {
        return get().createPanelEntity().apply(this.get(), pos, state);
    }

    public boolean is(@Nullable PanelEntity t) {
        return t != null && t.getType() == get();
    }

    public Optional<T> get(BlockGetter blockGetter, BlockPos pos, IPanelPosition panelPosition) {
        return Optional.ofNullable(getNullable(blockGetter, pos, panelPosition));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public T getNullable(BlockGetter blockGetter, BlockPos pos, IPanelPosition panelPosition) {
        if (blockGetter.getBlockEntity(pos) instanceof IFrameEntity frame) {
            PanelEntity panel = frame.getPanelEntity(panelPosition);
            if (panel != null) {
                if (is(panel)) {
                    return (T) panel;
                }
            }
        }
        return null;
    }

    public static <T extends PanelEntity> PanelEntityEntry<T> cast(RegistryEntry<PanelEntityType<T>> entry) {
        return RegistryEntry.cast(PanelEntityEntry.class, entry);
    }
}
