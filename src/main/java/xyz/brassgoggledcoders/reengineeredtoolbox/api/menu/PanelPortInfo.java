package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

import java.util.List;

public record PanelPortInfo(
        short menuId,
        List<Port> ports
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeShort(menuId());
        friendlyByteBuf.writeCollection(ports(), (listByteBuf, port) -> port.encode(listByteBuf));
    }

    public static PanelPortInfo decode(FriendlyByteBuf friendlyByteBuf) {
        return new PanelPortInfo(
                friendlyByteBuf.readShort(),
                friendlyByteBuf.readList(Port::decode)
        );
    }

    public record Port(
            String identifier,
            int connection,
            TypedSlotType backingSlot
    ) {

        public void encode(FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeUtf(identifier());
            friendlyByteBuf.writeInt(connection());
            friendlyByteBuf.writeRegistryId(TypedSlotTypes.getRegistry(), backingSlot());
        }

        public static Port decode(FriendlyByteBuf friendlyByteBuf) {
            return new Port(
                    friendlyByteBuf.readUtf(),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readRegistryId()
            );
        }
    }

}


