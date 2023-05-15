package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

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
    }


    public static void setup() {
        instance = new NetworkHandler();
    }

    public static NetworkHandler getInstance() {
        return instance;
    }
}
