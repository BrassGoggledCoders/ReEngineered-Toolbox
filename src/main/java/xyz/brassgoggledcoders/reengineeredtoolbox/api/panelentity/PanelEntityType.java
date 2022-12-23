package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity;

import com.mojang.datafixers.util.Function3;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import java.util.function.BiFunction;

public record PanelEntityType<T extends PanelEntity>(
        Function3<PanelEntityType<T>, IFrameEntity, PanelState, T> createPanelEntity
) {
}
