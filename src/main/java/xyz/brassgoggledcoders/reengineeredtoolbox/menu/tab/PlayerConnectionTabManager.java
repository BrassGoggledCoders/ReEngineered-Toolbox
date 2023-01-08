package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;

import java.util.HashMap;
import java.util.Map;

public abstract class PlayerConnectionTabManager {
    private int activeMenuId = -1;
    private final Map<Port, Integer> panelPorts = new HashMap<>();
    private String selectedPort = null;
    private TypedSlotHolderState typedSlotHolderState;

    public boolean isForMenu(@Nullable AbstractContainerMenu menu) {
        return menu != null && !this.getPanelPorts().isEmpty() && this.getActiveMenuId() == menu.containerId;
    }

    public int getActiveMenuId() {
        return activeMenuId;
    }

    public void setActiveMenuId(int activeMenuId) {
        this.activeMenuId = activeMenuId;
    }

    public String getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(String currentSelectedConnect) {
        this.selectedPort = currentSelectedConnect;
    }

    public Map<Port, Integer> getPanelPorts() {
        return panelPorts;
    }

    public void setPanelPorts(Map<Port, Integer> ports) {
        this.panelPorts.clear();
        this.panelPorts.putAll(ports);
    }

    public void clear() {
        this.getPanelPorts().clear();
        this.setSelectedPort(null);
    }

    public void setTypedSlotHolderState(TypedSlotHolderState holderState) {
        this.typedSlotHolderState = holderState;
    }

    public TypedSlotHolderState getTypedSlotHolderState() {
        return typedSlotHolderState;
    }

    public abstract void setPortConnection(String identifier, int connectionId);
}
