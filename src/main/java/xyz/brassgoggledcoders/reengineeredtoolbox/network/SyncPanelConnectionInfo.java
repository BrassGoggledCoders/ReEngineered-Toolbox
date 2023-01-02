package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelConnectionInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientConnectionTabManager;

import java.util.function.Supplier;

public record SyncPanelConnectionInfo(
        PanelConnectionInfo panelConnectionInfo
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        this.panelConnectionInfo().encode(friendlyByteBuf);
    }

    public void consume(Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        ClientConnectionTabManager.getInstance().setPanelConnectionInfo(this.panelConnectionInfo());
    }

    public static SyncPanelConnectionInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncPanelConnectionInfo(
                PanelConnectionInfo.decode(friendlyByteBuf)
        );
    }
}
