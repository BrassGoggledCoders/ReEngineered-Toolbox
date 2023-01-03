package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

public record TypedSlotHolderState(
        int height,
        int width,
        TypedSlotState[] slotStates
) {
}
