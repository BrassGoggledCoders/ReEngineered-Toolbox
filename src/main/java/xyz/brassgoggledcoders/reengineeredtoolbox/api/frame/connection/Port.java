package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public record Port(
        String identifier,
        Component description,
        TypedSlotType backingSlot
) {

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(identifier());
        friendlyByteBuf.writeComponent(this.description());
        friendlyByteBuf.writeRegistryId(TypedSlotTypes.getRegistry(), backingSlot());
    }

    public static Port decode(FriendlyByteBuf friendlyByteBuf) {
        return new Port(
                friendlyByteBuf.readUtf(),
                friendlyByteBuf.readComponent(),
                friendlyByteBuf.readRegistryId()
        );
    }
}


