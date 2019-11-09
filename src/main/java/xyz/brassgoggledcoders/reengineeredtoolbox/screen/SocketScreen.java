package xyz.brassgoggledcoders.reengineeredtoolbox.screen;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.GuiTileAddonScreen;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.SocketContainer;

import javax.annotation.Nonnull;
import java.util.List;

public class SocketScreen extends GuiTileAddonScreen implements IHasContainer<SocketContainer> {
    private final SocketContainer container;

    public SocketScreen(SocketContainer container) {
        super(container.getSocketTileEntity(), IAssetProvider.DEFAULT_PROVIDER, true);
        this.container = container;
    }

    @Override
    public List<IFactory<IGuiAddon>> guiAddons() {
        return null;
    }

    public static SocketScreen create(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        return new SocketScreen(container);
    }

    @Override
    @Nonnull
    public SocketContainer getContainer() {
        return container;
    }
}
