package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelPortInfo;

import java.util.Collections;
import java.util.List;

public class PlayerConnectionTabManager {

    private PanelPortInfo panelPortInfo = null;
    private String selectedConnection = null;

    public boolean isForMenu(@Nullable AbstractContainerMenu menu) {
        return menu != null && this.getPanelConnectionInfo() != null && this.getPanelConnectionInfo().menuId() == menu.containerId;
    }

    public PanelPortInfo getPanelConnectionInfo() {
        return panelPortInfo;
    }

    public void setPanelConnectionInfo(PanelPortInfo panelPortInfo) {
        this.panelPortInfo = panelPortInfo;
    }

    public String getSelectedConnection() {
        return selectedConnection;
    }

    public void setSelectedConnection(String currentSelectedConnect) {
        this.selectedConnection = currentSelectedConnect;
    }

    public List<PanelPortInfo.Port> getConnections() {
        return this.getPanelConnectionInfo() != null ? this.getPanelConnectionInfo().ports() : Collections.emptyList();
    }

    public void clear() {
        this.setPanelConnectionInfo(null);
        this.setSelectedConnection(null);
    }
}
