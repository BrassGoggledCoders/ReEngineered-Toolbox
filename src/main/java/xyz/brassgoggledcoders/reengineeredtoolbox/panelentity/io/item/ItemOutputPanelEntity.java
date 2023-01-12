package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.MovingConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;

public class ItemOutputPanelEntity extends ItemIOPanelEntity {

    public ItemOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.ITEM_OUTPUT.getPanelEntityType(), frameEntity, panelState);
    }

    public ItemOutputPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    @Override
    protected MovingConnection<IItemTypedSlot, ItemStack, IItemHandler> createConnection() {
        return MovingConnection.itemConnection(
                this.getFrameEntity().getTypedSlotHolder(),
                this.getPort(),
                this::getSlotForMenu,
                MovingConnection.ConnectionDirection.FROM_SLOT
        );
    }
}
