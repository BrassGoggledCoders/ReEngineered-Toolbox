package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;

public class ClientPlayerConnectionTabManager extends PlayerConnectionTabManager {
    private static final PlayerConnectionTabManager INSTANCE = new PlayerConnectionTabManager();

    @Override
    public void setSelectedConnection(String currentSelectedConnect) {
        super.setSelectedConnection(currentSelectedConnect);
        this.sendSelectionToServer();
    }

    public void sendSelectionToServer() {
        NetworkHandler.getInstance()
                .syncPanelConnectionSelect(this.getSelectedConnection());
    }

    public static PlayerConnectionTabManager getInstance() {
        return INSTANCE;
    }
}
