package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientPlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.PlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;

import java.util.Map;
import java.util.function.Supplier;

public record SyncPortTabInfo(
        int menuId,
        Map<Port, Integer> panelPortInfo,
        TypedSlotHolderState holderState
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(menuId());
        friendlyByteBuf.writeMap(panelPortInfo(), (keyBuf, port) -> port.encode(keyBuf), FriendlyByteBuf::writeInt);
        holderState().encode(friendlyByteBuf);
    }

    public void consume(Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        PlayerConnectionTabManager playerConnectionTabManager = ClientPlayerConnectionTabManager.getInstance();
        playerConnectionTabManager.setActiveMenuId(this.menuId());
        playerConnectionTabManager.setPanelPorts(this.panelPortInfo());
        playerConnectionTabManager.setTypedSlotHolderState(this.holderState());
    }

    public static SyncPortTabInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncPortTabInfo(
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readMap(Port::decode, FriendlyByteBuf::readInt),
                TypedSlotHolderState.decode(friendlyByteBuf)
        );
    }
}
