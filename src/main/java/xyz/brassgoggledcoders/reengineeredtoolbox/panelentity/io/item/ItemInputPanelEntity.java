package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.item;

import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class ItemInputPanelEntity extends ItemIOPanelEntity {

    public ItemInputPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState, ReEngineeredText.ITEM_SLOT_IN);
    }

    @Override
    protected IOStyle getIOStyle() {
        return IOStyle.ONLY_INSERT;
    }
}
