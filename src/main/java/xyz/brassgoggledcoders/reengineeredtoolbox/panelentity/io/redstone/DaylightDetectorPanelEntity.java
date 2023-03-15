package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.io.redstone;

import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.ListeningConnection;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.IRedstoneTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneTypedSlot;

import java.util.HashMap;
import java.util.Map;

public class DaylightDetectorPanelEntity extends RedstoneIOPanelEntity {
    private final Port invertedPort;
    private final ListeningConnection<IRedstoneTypedSlot, Integer> invertedConnection;

    public DaylightDetectorPanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
        this.invertedPort = new Port(
                "invertedRedstone",
                null,
                TypedSlotTypes.REDSTONE.get()
        );
        this.invertedConnection = ListeningConnection.redstoneSupplier(
                this.getFrameEntity().getTypedSlotHolder(),
                this.invertedPort,
                this::getInvertedPower
        );
    }

    public DaylightDetectorPanelEntity(IFrameEntity iFrameEntity, PanelState panelState) {
        this(ReEngineeredPanels.DAYLIGHT_DETECTOR.getPanelEntityType(), iFrameEntity, panelState);
    }

    @Override
    protected ListeningConnection<IRedstoneTypedSlot, Integer> createConnection() {
        return ListeningConnection.redstoneSupplier(
                this.getFrameEntity().getTypedSlotHolder(),
                this.getPort(),
                this::getPower
        );
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
            this.getConnection()
                    .checkUpdate();
        }
    }

    @Override
    public IRedstoneTypedSlot getSlotForMenu() {
        IRedstoneTypedSlot redstoneTypedSlot = new RedstoneTypedSlot();
        redstoneTypedSlot.addSupplier(this.getPort(), this::getPower);
        return redstoneTypedSlot;
    }

    @Override
    @NotNull
    public Map<Port, Integer> getPorts() {
        Map<Port, Integer> ports = new HashMap<>(super.getPorts());
        ports.put(this.invertedPort, this.invertedConnection.getSlotId());
        return ports;
    }

    public int getInvertedPower() {
        return 15 - this.getPower();
    }
}
