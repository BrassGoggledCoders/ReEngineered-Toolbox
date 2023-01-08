package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotState;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

public class ServerPlayerConnectionTabManager extends PlayerConnectionTabManager {
    private final WeakReference<ServerPlayer> serverPlayerWeakReference;
    private final WeakReference<IFrameEntity> frameEntityWeakReference;
    private final WeakReference<PanelEntity> panelEntityWeakReference;

    public ServerPlayerConnectionTabManager(ServerPlayer serverPlayer, IFrameEntity frame, PanelEntity panelEntity) {
        this.serverPlayerWeakReference = new WeakReference<>(serverPlayer);
        this.frameEntityWeakReference = new WeakReference<>(frame);
        this.panelEntityWeakReference = new WeakReference<>(panelEntity);
        this.setPanelPorts(panelEntity.getPorts());
    }

    @Override
    public void setPanelPorts(Map<Port, Integer> ports) {
        super.setPanelPorts(ports);
        if (this.getActiveMenuId() >= 0) {
            this.sync();
        }
    }

    @Override
    public void setActiveMenuId(int activeMenuId) {
        super.setActiveMenuId(activeMenuId);
        if (!this.getPanelPorts().isEmpty()) {
            this.sync();
        }
    }

    public void sync() {
        ServerPlayer serverPlayer = serverPlayerWeakReference.get();
        if (serverPlayer != null) {
            NetworkHandler.getInstance()
                    .syncPortTabInfo(serverPlayer, this.getActiveMenuId(), this.getPanelPorts(), this.getTypedSlotHolderState());
        }
    }

    @Override
    public TypedSlotHolderState getTypedSlotHolderState() {
        if (super.getTypedSlotHolderState() == null) {
            IFrameEntity frameEntity = this.getFrameEntity();
            if (frameEntity != null) {
                this.setTypedSlotHolderState(frameEntity.getTypedSlotHolder()
                        .getState()
                );
            } else {
                this.setTypedSlotHolderState(new TypedSlotHolderState(
                        0,
                        0,
                        new TypedSlotState[0]
                ));
            }
        }

        return super.getTypedSlotHolderState();
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

    @Override
    public void setPortConnection(String identifier, int connectionId) {
        IFrameEntity frameEntity = this.getFrameEntity();
        PanelEntity panelEntity = this.panelEntityWeakReference.get();
        if (frameEntity != null && panelEntity != null) {
            Iterator<Port> portIterator = this.getPanelPorts()
                    .keySet()
                    .iterator();

            Port port = null;

            while (port == null && portIterator.hasNext()) {
                Port checkedPort = portIterator.next();
                if (checkedPort.identifier().equals(identifier)) {
                    port = checkedPort;
                }
            }

            if (port != null) {
                ITypedSlotHolder typedSlotHolder = frameEntity.getTypedSlotHolder();

                if (connectionId >= 0 && connectionId < typedSlotHolder.getSize()) {
                    ITypedSlot<?> typedSlot = typedSlotHolder.getSlot(connectionId);
                    if (typedSlot.getType() != port.backingSlot()) {
                        if (typedSlot.isEmpty()) {
                            typedSlotHolder.setSlot(connectionId, port.backingSlot().createSlot());
                            panelEntity.setPortConnection(port, connectionId);
                        }
                    } else {
                        panelEntity.setPortConnection(port, connectionId);
                    }
                }
            }
            this.setPanelPorts(panelEntity.getPorts());
        }
    }

    private boolean isCloseEnough(Player player) {
        IFrameEntity frameEntity = this.getFrameEntity();
        if (frameEntity != null) {
            return player.distanceToSqr(Vec3.atCenterOf(frameEntity.getFramePos())) <= 64;
        } else {
            return false;
        }
    }

    public boolean isValid() {
        Player player = this.getPlayer();
        return player != null && this.getFrameEntity() != null && this.panelEntityWeakReference.get() != null &&
                this.isForMenu(player.containerMenu) && isCloseEnough(player);
    }

    public void tick() {
        IFrameEntity frameEntity = this.getFrameEntity();
        boolean sync = false;
        if (frameEntity != null) {
            ITypedSlotHolder typedSlotHolder = this.getFrameEntity().getTypedSlotHolder();
            if (super.getTypedSlotHolderState() == null) {
                sync = true;
            }
            if (!typedSlotHolder.matches(this.getTypedSlotHolderState())) {
                this.setTypedSlotHolderState(null);
                sync = true;
            }
        }
        if (sync) {
            this.sync();
        }
    }
}
