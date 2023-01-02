package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelConnectionInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;

import java.util.Collections;
import java.util.List;

public class ClientConnectionTabManager {
    private static final ClientConnectionTabManager INSTANCE = new ClientConnectionTabManager();

    private PanelConnectionInfo panelConnectionInfo = null;
    private String selectedConnection = null;

    public boolean isForMenu(AbstractContainerMenu menu) {
        return this.getPanelConnectionInfo() != null && this.getPanelConnectionInfo().menuId() == menu.containerId;
    }

    public PanelConnectionInfo getPanelConnectionInfo() {
        return panelConnectionInfo;
    }

    public void setPanelConnectionInfo(PanelConnectionInfo panelConnectionInfo) {
        this.panelConnectionInfo = panelConnectionInfo;
    }

    public String getSelectedConnection() {
        return selectedConnection;
    }

    public void setSelectedConnection(String currentSelectedConnect) {
        this.selectedConnection = currentSelectedConnect;
        this.sendSelectionToServer();
    }

    public List<PanelConnectionInfo.Connection> getConnections() {
        return this.getPanelConnectionInfo() != null ? this.getPanelConnectionInfo().connections() : Collections.emptyList();
    }

    public void clear() {
        this.setPanelConnectionInfo(null);
        this.setSelectedConnection(null);
    }

    public void sendSelectionToServer() {
        NetworkHandler.getInstance()
                .syncPanelConnectionSelect(this.getSelectedConnection());
    }

    public static ClientConnectionTabManager getInstance() {
        return INSTANCE;
    }
}
