package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public record FrameSlotView(
        float xPos,
        float yPos,
        float height,
        float width
) {

    public boolean isInside(Vec3 look, Direction direction) {
        double xLook;
        double yLook;

        if (direction.getAxis().isHorizontal()) {
            yLook = look.y() - (int) look.y();
        } else {
            yLook = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? look.z() : -look.z();
        }

        yLook = Math.abs(yLook);
        yLook = yLook - (int) yLook;
        yLook *= 16D;

        xLook = switch (direction.getAxis()) {
            case X -> look.z();
            case Y -> direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? look.x() : -look.x();
            case Z -> look.x();
        };

        xLook = Math.abs(xLook);
        xLook = xLook - (int) xLook;
        xLook *= 16D;

        if (direction == Direction.WEST || direction == Direction.SOUTH || direction == Direction.DOWN) {
            xLook = 16 - xLook;
        }

        return xLook > this.xPos() && xLook < this.xPos() + this.width() &&
                yLook > this.yPos() && yLook < this.yPos() + this.height();
    }
}
