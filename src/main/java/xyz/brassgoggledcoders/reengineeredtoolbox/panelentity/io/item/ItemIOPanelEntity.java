package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item.FrequencyBackedItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.IOPanelEntity;

public abstract class ItemIOPanelEntity extends IOPanelEntity {

    private final LazyOptional<IItemHandler> lazyOptional;

    public ItemIOPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.lazyOptional = LazyOptional.of(this::createItemHandler);
    }

    private FrequencyBackedItemHandler createItemHandler() {
        return new FrequencyBackedItemHandler(
                this.getIoPort(),
                this.getFrameEntity().getCapability(ReEngineeredCapabilities.FREQUENCY_ITEM_HANDLER),
                this.getIOStyle()
        );
    }

    protected abstract IOStyle getIOStyle();

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.lazyOptional.invalidate();
    }
}
