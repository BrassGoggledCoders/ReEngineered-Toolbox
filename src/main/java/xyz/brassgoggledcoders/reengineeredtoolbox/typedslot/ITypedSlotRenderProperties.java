package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

public interface ITypedSlotRenderProperties {
    void renderSlot(Screen screen, PoseStack poseStack, int slotX, int slotY, ITypedSlot<?> typedSlot);
}