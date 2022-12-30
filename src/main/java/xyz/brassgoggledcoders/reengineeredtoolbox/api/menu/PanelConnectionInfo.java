package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;

import java.util.List;

public record PanelConnectionInfo(
        short menuId,
        List<Connection> connections
) {
    public record Connection(
            String identifier,
            TypedSlotType backingSlot
    ) {

    }
}


