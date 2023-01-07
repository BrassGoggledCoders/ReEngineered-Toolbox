package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public record TypedSlotHolderState(
        int height,
        int width,
        TypedSlotState[] slotStates
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.height());
        friendlyByteBuf.writeInt(this.width());
        friendlyByteBuf.writeCollection(List.of(slotStates()), (listByteBuf, slotState) -> slotState.encode(listByteBuf));
    }

    public static TypedSlotHolderState decode(FriendlyByteBuf friendlyByteBuf) {
        return new TypedSlotHolderState(
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readList(TypedSlotState::decode)
                        .toArray(TypedSlotState[]::new)
        );
    }
}
