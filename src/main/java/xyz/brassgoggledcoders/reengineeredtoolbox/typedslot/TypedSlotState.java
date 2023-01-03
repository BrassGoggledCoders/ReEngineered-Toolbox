package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.network.FriendlyByteBuf;

public record TypedSlotState(
        TypedSlotType type,
        boolean empty
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeRegistryId(TypedSlotTypes.getRegistry(), this.type());
        friendlyByteBuf.writeBoolean(this.empty());
    }

    public static TypedSlotState decode(FriendlyByteBuf friendlyByteBuf) {
        return new TypedSlotState(
                friendlyByteBuf.readRegistryId(),
                friendlyByteBuf.readBoolean()
        );
    }
}
