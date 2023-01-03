package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ServerConnectionTabManager;

import java.util.function.Supplier;

public record UpdatePortSelection(
        @Nullable
        String selection
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeNullable(this.selection(), (FriendlyByteBuf::writeUtf));
    }

    public void consume(Supplier<NetworkEvent.Context> context) {
        ServerConnectionTabManager.getInstance()
                .getForPlayer(context.get().getSender())
                .ifPresent(tabManager -> tabManager.setSelectedPort(this.selection()));
    }

    public static UpdatePortSelection decode(FriendlyByteBuf friendlyByteBuf) {
        return new UpdatePortSelection(
                friendlyByteBuf.readNullable(FriendlyByteBuf::readUtf)
        );
    }
}
