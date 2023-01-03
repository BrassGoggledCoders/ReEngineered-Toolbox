package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ServerConnectionTabManager;

import java.util.function.Supplier;

public record UpdatePortConnection(
        @NotNull
        String identifier,
        int connectionId
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.identifier());
        friendlyByteBuf.writeInt(this.connectionId());
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer serverPlayer = contextSupplier.get().getSender();
        ServerConnectionTabManager.getInstance()
                .getForPlayer(serverPlayer)
                .ifPresent(playerConnectionTabManager -> playerConnectionTabManager.setPortConnection(
                        this.identifier(),
                        this.connectionId()
                ));
    }

    public static UpdatePortConnection decode(FriendlyByteBuf friendlyByteBuf) {
        return new UpdatePortConnection(
                friendlyByteBuf.readUtf(),
                friendlyByteBuf.readInt()
        );
    }
}
