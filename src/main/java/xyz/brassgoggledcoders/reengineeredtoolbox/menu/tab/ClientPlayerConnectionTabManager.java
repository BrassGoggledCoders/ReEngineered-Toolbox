package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;

public class ClientPlayerConnectionTabManager extends PlayerConnectionTabManager {
    private static final PlayerConnectionTabManager INSTANCE = new ClientPlayerConnectionTabManager();

    @Override
    public void setSelectedPort(String currentSelectedConnect) {
        super.setSelectedPort(currentSelectedConnect);
        this.sendSelectionToServer();
    }

    public void sendSelectionToServer() {
        NetworkHandler.getInstance()
                .syncPanelConnectionSelect(this.getSelectedPort());
    }

    public void setPortConnection(String identifier, int connectionId) {
        NetworkHandler.getInstance()
                .updatePortConnect(identifier, connectionId);
        for (int x = 0; x < this.getPanelPorts().size(); x++) {
            Port port = this.getPanelPorts().get(x);
            if (port.identifier().equals(identifier)) {
                this.getPanelPorts().set(x, port.withConnection(connectionId));
            }
        }
    }

    public static PlayerConnectionTabManager getInstance() {
        return INSTANCE;
    }
}
