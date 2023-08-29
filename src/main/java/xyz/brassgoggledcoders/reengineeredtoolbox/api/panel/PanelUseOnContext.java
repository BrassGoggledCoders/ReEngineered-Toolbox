package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.util.VectorHelper;

public record PanelUseOnContext(
        Entity entity,
        InteractionHand hand,
        IFrameEntity frameEntity,
        PanelHitResult hitResult
) {

    public IPanelPosition getPanelPosition() {
        return this.hitResult.panelPosition();
    }

    public static PanelUseOnContext from(UseOnContext context, IFrameEntity frameEntity) {
        return new PanelUseOnContext(
                context.getPlayer(),
                context.getHand(),
                frameEntity,
                new PanelHitResult(
                        BlockPanelPosition.fromDirection(context.getClickedFace()),
                        VectorHelper.convertToVec2(context.getClickLocation(), context.getClickedFace())
                )
        );
    }
}
