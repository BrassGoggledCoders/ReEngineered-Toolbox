package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerConnectionTabManager {
    private short activeMenuId = -1;
    private final List<Port> panelPorts = new ArrayList<>();
    private String selectedPort = null;
    private TypedSlotHolderState typedSlotHolderState;

    public boolean isForMenu(@Nullable AbstractContainerMenu menu) {
        return menu != null && !this.getPanelPorts().isEmpty() && this.getActiveMenuId() == menu.containerId;
    }

    public short getActiveMenuId() {
        return activeMenuId;
    }

    public void setActiveMenuId(short activeMenuId) {
        this.activeMenuId = activeMenuId;
    }

    public String getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(String currentSelectedConnect) {
        this.selectedPort = currentSelectedConnect;
    }

    public List<Port> getPanelPorts() {
        return panelPorts;
    }

    public void setPanelPorts(List<Port> ports) {
        this.panelPorts.clear();
        this.panelPorts.addAll(ports);
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

    public void setPortConnection(String identifier, int connectionId) {

    }

    @Nullable
    public Port getSelectedPortValue() {
        return this.getPanelPorts()
                .stream()
                .filter(port -> port.identifier().equals(this.getSelectedPort()))
                .findFirst()
                .orElse(null);
    }
}
