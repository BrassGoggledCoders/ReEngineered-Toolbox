package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientPlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.PlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;

import java.util.List;
import java.util.function.Supplier;

public record SyncPortTabInfo(
        List<Port> panelPortInfo,
        TypedSlotHolderState holderState
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeCollection(panelPortInfo(), (listBuf, port) -> port.encode(listBuf));
        holderState().encode(friendlyByteBuf);
    }

    public void consume(Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        PlayerConnectionTabManager playerConnectionTabManager = ClientPlayerConnectionTabManager.getInstance();
        playerConnectionTabManager.setPanelPorts(this.panelPortInfo());
        playerConnectionTabManager.setTypedSlotHolderState(this.holderState());
    }

    public static SyncPortTabInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncPortTabInfo(
                friendlyByteBuf.readList(Port::decode),
                TypedSlotHolderState.decode(friendlyByteBuf)
        );
    }
}
