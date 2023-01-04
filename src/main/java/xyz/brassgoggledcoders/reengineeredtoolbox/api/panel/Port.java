package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public record Port(
        String identifier,
        Component description,
        int connection,
        TypedSlotType backingSlot
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(identifier());
        friendlyByteBuf.writeComponent(this.description());
        friendlyByteBuf.writeInt(connection());
        friendlyByteBuf.writeRegistryId(TypedSlotTypes.getRegistry(), backingSlot());
    }

    public Port withConnection(int connectionId) {
        return new Port(
                this.identifier(),
                this.description(),
                connectionId,
                this.backingSlot()
        );
    }

    public static Port decode(FriendlyByteBuf friendlyByteBuf) {
        return new Port(
                friendlyByteBuf.readUtf(),
                friendlyByteBuf.readComponent(),
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readRegistryId()
        );
    }
}


