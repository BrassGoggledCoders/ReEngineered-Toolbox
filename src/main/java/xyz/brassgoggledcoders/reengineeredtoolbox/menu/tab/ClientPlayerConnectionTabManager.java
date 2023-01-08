package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

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
        this.getPanelPorts()
                .keySet()
                .stream()
                .filter(port -> port.identifier().equals(identifier))
                .findFirst()
                .ifPresent(port -> this.getPanelPorts().put(port, connectionId));
    }

    public static PlayerConnectionTabManager getInstance() {
        return INSTANCE;
    }
}
