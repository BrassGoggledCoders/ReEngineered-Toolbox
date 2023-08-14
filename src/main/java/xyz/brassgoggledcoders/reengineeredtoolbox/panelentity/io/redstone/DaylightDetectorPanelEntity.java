package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlotViews;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class DaylightDetectorPanelEntity extends RedstoneIOPanelEntity {
    private final FrameSlot invertedPort;

    public DaylightDetectorPanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState, ReEngineeredText.REDSTONE_SLOT_IN);
        this.invertedPort = new FrameSlot(ReEngineeredText.REDSTONE_SLOT_INVERTED_OUT, FrameSlotViews.CENTERED_4X4);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        int newPower = 0;
        if (this.getLevel().dimensionType().hasSkyLight()) {
            if (this.getLevel().getGameTime() % 20L == 0L) {
                int redstonePower = this.getLevel().getBrightness(LightLayer.SKY, this.getBlockPos()) - this.getLevel().getSkyDarken();
                float f = this.getLevel().getSunAngle(1.0F);
                if (redstonePower > 0) {
                    float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                    f += (f1 - f) * 0.2F;
                    redstonePower = Math.round((float) redstonePower * Mth.cos(f));
                }

                newPower = Mth.clamp(redstonePower, 0, 15);
            }
        }

        if (newPower != this.getPower()) {
            this.setPower(newPower);
            //this.getConnection()
            //        .checkUpdate();
        }
    }

    public int getInvertedPower() {
        return 15 - this.getPower();
    }
}
