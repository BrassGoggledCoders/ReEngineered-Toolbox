package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredText;

public class EnergyOutputPanelEntity extends EnergyIOPanelEntity {
    private int coolDown;

    public EnergyOutputPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState, IOStyle.ONLY_EXTRACT, ReEngineeredText.ENERGY_SLOT_OUT);
    }

    @Override
    public void serverTick() {
        if (coolDown <= 0) {
            int maxMove = this.getEnergyStorage().extractEnergy(2000, true);
            if (maxMove > 0) {
                BlockEntity blockEntity = this.getAdjacantBlockEntity();
                if (blockEntity != null) {
                    blockEntity.getCapability(ForgeCapabilities.ENERGY)
                            .ifPresent(neighborStorage -> {
                                int transferred = neighborStorage.receiveEnergy(maxMove, false);
                                this.getEnergyStorage()
                                        .extractEnergy(transferred, false);
                                if (transferred == 0) {
                                    this.coolDown = 10;
                                }
                            });
                } else {
                    coolDown = 10;
                }
            }

        } else {
            coolDown--;
        }
    }
}
