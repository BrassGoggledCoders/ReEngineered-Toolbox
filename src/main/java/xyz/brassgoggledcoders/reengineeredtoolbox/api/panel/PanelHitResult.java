package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.world.phys.Vec2;

public record PanelHitResult(
        IPanelPosition panelPosition,
        Vec2 location
) {

}
