package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public interface ITypedSlotRenderProperties {
    default void renderSlot(Screen screen, PoseStack poseStack, int slotX, int slotY, ITypedSlot<?> typedSlot) {

    }

    default void getTooltip(List<TooltipComponent> tooltipComponentList, ITypedSlot<?> typedSlot) {

    }
}