package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import net.minecraft.core.Direction;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;

import java.util.Map;

public record FramePanelKey(
        PanelState[] panelStates
) {
    public ModelData toModelData() {
        ModelData.Builder builder = ModelData.builder();
        for (Map.Entry<Direction, ModelProperty<PanelState>> entry : FrameBlockEntity.PANEL_STATE_MODEL_PROPERTIES.entrySet()) {
            builder.with(entry.getValue(), panelStates[entry.getKey().ordinal()]);
        }
        return builder.build();
    }
}
