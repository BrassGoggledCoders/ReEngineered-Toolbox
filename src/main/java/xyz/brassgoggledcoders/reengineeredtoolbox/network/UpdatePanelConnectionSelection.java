package xyz.brassgoggledcoders.reengineeredtoolbox.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ServerConnectionTabManager;

import java.util.function.Supplier;

public record UpdatePanelConnectionSelection(
        @Nullable
        String selection
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeNullable(this.selection(), (FriendlyByteBuf::writeUtf));
    }

    public void consume(Supplier<NetworkEvent.Context> context) {
        ServerConnectionTabManager.getInstance()
                .getForPlayer(context.get().getSender())
                .ifPresent(tabManager -> tabManager.setSelectedConnection(this.selection()));
    }

    public static UpdatePanelConnectionSelection decode(FriendlyByteBuf friendlyByteBuf) {
        return new UpdatePanelConnectionSelection(
                friendlyByteBuf.readNullable(FriendlyByteBuf::readUtf)
        );
    }
}
