package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelPortInfo;

public class NetworkHandler {
    private static final String VERSION = "1";
    private static NetworkHandler instance;

    private final SimpleChannel channel;

    private NetworkHandler() {
        this.channel = NetworkRegistry.ChannelBuilder.named(ReEngineeredToolbox.rl("main"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equals)
                .serverAcceptedVersions(VERSION::equals)
                .simpleChannel();

        this.channel.messageBuilder(SyncPanelConnectionInfo.class, 0)
                .encoder(SyncPanelConnectionInfo::encode)
                .decoder(SyncPanelConnectionInfo::decode)
                .consumerMainThread(SyncPanelConnectionInfo::consume)
                .add();

        this.channel.messageBuilder(UpdatePanelConnectionSelection.class, 1)
                .encoder(UpdatePanelConnectionSelection::encode)
                .decoder(UpdatePanelConnectionSelection::decode)
                .consumerMainThread(UpdatePanelConnectionSelection::consume)
                .add();
    }

    public void syncPanelConnectionInfo(ServerPlayer serverPlayer, PanelPortInfo panelPortInfo) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncPanelConnectionInfo(panelPortInfo));
    }

    public void syncPanelConnectionSelect(@Nullable String selectedConnection) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new UpdatePanelConnectionSelection(selectedConnection));
    }

    public static void setup() {
        instance = new NetworkHandler();
    }

    public static NetworkHandler getInstance() {
        return instance;
    }
}
