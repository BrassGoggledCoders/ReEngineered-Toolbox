package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;

import java.util.List;

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

        this.channel.messageBuilder(SyncPortTabInfo.class, 0)
                .encoder(SyncPortTabInfo::encode)
                .decoder(SyncPortTabInfo::decode)
                .consumerMainThread(SyncPortTabInfo::consume)
                .add();

        this.channel.messageBuilder(UpdatePortSelection.class, 1)
                .encoder(UpdatePortSelection::encode)
                .decoder(UpdatePortSelection::decode)
                .consumerMainThread(UpdatePortSelection::consume)
                .add();

        this.channel.messageBuilder(UpdatePortConnection.class, 2)
                .encoder(UpdatePortConnection::encode)
                .decoder(UpdatePortConnection::decode)
                .consumerMainThread(UpdatePortConnection::consume)
                .add();
    }

    public void syncPortTabInfo(ServerPlayer serverPlayer, List<Port> panelPortInfo, TypedSlotHolderState holderState) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncPortTabInfo(panelPortInfo, holderState));
    }

    public void syncPanelConnectionSelect(@Nullable String selectedConnection) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new UpdatePortSelection(selectedConnection));
    }

    public void updatePortConnect(String identifier, int connectionId) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new UpdatePortConnection(identifier, connectionId));
    }

    public static void setup() {
        instance = new NetworkHandler();
    }

    public static NetworkHandler getInstance() {
        return instance;
    }
}
