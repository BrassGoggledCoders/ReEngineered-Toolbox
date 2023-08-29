package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.util.VectorHelper;

public record FrameSlotView(
        float xPos,
        float yPos,
        float height,
        float width
) {

    public boolean isInside(Vec3 look3, Direction direction) {
        Vec2 panelLook = VectorHelper.convertToVec2(look3, direction);

        double xLook = panelLook.x;
        double yLook = panelLook.y;

        return xLook > this.xPos() && xLook < this.xPos() + this.width() &&
                yLook > this.yPos() && yLook < this.yPos() + this.height();
    }
}
