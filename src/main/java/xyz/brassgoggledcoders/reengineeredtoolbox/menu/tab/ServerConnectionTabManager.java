package xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ServerConnectionTabManager {
    private static final ServerConnectionTabManager INSTANCE = new ServerConnectionTabManager();

    private final Map<Player, ServerPlayerConnectionTabManager> playerConnectionTabManagers;

    public ServerConnectionTabManager() {
        this.playerConnectionTabManagers = new WeakHashMap<>();
    }

    public void startSession(ServerPlayer serverPlayer, IFrameEntity frame, PanelEntity panelEntity) {
        this.playerConnectionTabManagers.put(serverPlayer, new ServerPlayerConnectionTabManager(serverPlayer, frame, panelEntity));
    }

    public Optional<? extends PlayerConnectionTabManager> getForPlayer(Player player) {
        ServerPlayerConnectionTabManager tabManager = this.playerConnectionTabManagers.get(player);
        return Optional.ofNullable(tabManager);
    }

    public void tick() {
        Iterator<ServerPlayerConnectionTabManager> iterator = this.playerConnectionTabManagers.values().iterator();
        while (iterator.hasNext()) {
            ServerPlayerConnectionTabManager serverPlayerConnectionTabManager = iterator.next();
            if (serverPlayerConnectionTabManager.isValid()) {
                serverPlayerConnectionTabManager.tick();
            } else {
                iterator.remove();
            }
        }
    }

    public static ServerConnectionTabManager getInstance() {
        return INSTANCE;
    }
}
