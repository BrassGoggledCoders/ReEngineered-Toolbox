package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelPortInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;

import java.lang.ref.WeakReference;

public class ServerPlayerConnectionTabManager extends PlayerConnectionTabManager {
    private final WeakReference<ServerPlayer> serverPlayerWeakReference;
    private final WeakReference<IFrameEntity> frameEntityWeakReference;
    private final WeakReference<PanelEntity> panelEntityWeakReference;

    public ServerPlayerConnectionTabManager(ServerPlayer serverPlayer, IFrameEntity frame, PanelEntity panelEntity) {
        this.serverPlayerWeakReference = new WeakReference<>(serverPlayer);
        this.frameEntityWeakReference = new WeakReference<>(frame);
        this.panelEntityWeakReference = new WeakReference<>(panelEntity);
    }

    @Override
    public void setPanelConnectionInfo(PanelPortInfo panelPortInfo) {
        super.setPanelConnectionInfo(panelPortInfo);
        this.sync();
    }

    public void sync() {
        ServerPlayer serverPlayer = serverPlayerWeakReference.get();
        if (serverPlayer != null) {
            NetworkHandler.getInstance()
                    .syncPanelConnectionInfo(serverPlayer, this.getPanelConnectionInfo());
        }
    }

    @Nullable
    private ServerPlayer getPlayer() {
        return serverPlayerWeakReference.get();
    }

    @Nullable
    private IFrameEntity getFrameEntity() {
        IFrameEntity frameEntity = this.frameEntityWeakReference.get();
        if (frameEntity != null && frameEntity.isValid()) {
            return frameEntity;
        }
        return null;
    }

    public boolean isValid() {
        Player player = this.getPlayer();
        return player != null && this.getFrameEntity() != null && this.panelEntityWeakReference.get() != null &&
                this.isForMenu(player.containerMenu);
    }

    public void tick() {
        IFrameEntity frameEntity = this.getFrameEntity();
        if (frameEntity != null) {
            ITypedSlotHolder typedSlotHolder = frameEntity.getTypedSlotHolder();

        }
    }
}
