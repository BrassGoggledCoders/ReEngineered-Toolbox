package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

import java.util.List;

public record PanelConnectionInfo(
        short menuId,
        List<Connection> connections
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeShort(menuId());
        friendlyByteBuf.writeCollection(connections(), (listByteBuf, connection) -> connection.encode(listByteBuf));
    }

    public static PanelConnectionInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new PanelConnectionInfo(
                friendlyByteBuf.readShort(),
                friendlyByteBuf.readList(Connection::decode)
        );
    }

    public record Connection(
            String identifier,
            TypedSlotType backingSlot
    ) {

        public void encode(FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeUtf(identifier());
            friendlyByteBuf.writeRegistryId(TypedSlotTypes.getRegistry(), backingSlot());
        }

        public static Connection decode(FriendlyByteBuf friendlyByteBuf) {
            return new Connection(
                    friendlyByteBuf.readUtf(),
                    friendlyByteBuf.readRegistryId()
            );
        }
    }

}


