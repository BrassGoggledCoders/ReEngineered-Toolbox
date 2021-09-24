package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.network.PacketBuffer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PanelInfo {
    private final PanelState panelState;
    private final PanelEntity panelEntity;
    private final UUID uniqueId;

    public PanelInfo(UUID uniqueId, PanelState panelState) {
        this(uniqueId, panelState, null);
    }

    public PanelInfo(UUID uniqueId, PanelState panelState, PanelEntity panelEntity) {
        this.panelState = panelState;
        this.panelEntity = panelEntity;
        this.uniqueId = uniqueId;
    }


    @Nonnull
    public PanelState getPanelState() {
        return panelState;
    }

    @Nullable
    public PanelEntity getPanelEntity() {
        return panelEntity;
    }

    @Nonnull
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void toBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeUUID(uniqueId);
        packetBuffer.writeVarInt(RETRegistries.getPanelStateIdentities().getId(panelState));
    }

    public static PanelInfo fromBuffer(PacketBuffer buffer) {
        return new PanelInfo(
                buffer.readUUID(),
                RETRegistries.getPanelStateIdentities().byId(buffer.readVarInt())
        );
    }
}
