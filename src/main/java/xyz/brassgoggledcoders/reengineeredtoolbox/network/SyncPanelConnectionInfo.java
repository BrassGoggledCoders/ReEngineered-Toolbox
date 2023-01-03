package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelPortInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientPlayerConnectionTabManager;

import java.util.function.Supplier;

public record SyncPanelConnectionInfo(
        PanelPortInfo panelPortInfo
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        this.panelPortInfo().encode(friendlyByteBuf);
    }

    public void consume(Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        ClientPlayerConnectionTabManager.getInstance()
                .setPanelConnectionInfo(this.panelPortInfo());
    }

    public static SyncPanelConnectionInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncPanelConnectionInfo(
                PanelPortInfo.decode(friendlyByteBuf)
        );
    }
}
