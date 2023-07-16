package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class ItemOutputPanelEntity extends ItemIOPanelEntity {

    public ItemOutputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(ReEngineeredPanels.ITEM_OUTPUT.getPanelEntityType(), frameEntity, panelState, ReEngineeredText.ITEM_SLOT_OUT);
    }

    public ItemOutputPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState, ReEngineeredText.ITEM_SLOT_OUT);
    }

    @Override
    protected IOStyle getIOStyle() {
        return IOStyle.ONLY_EXTRACT;
    }
}
