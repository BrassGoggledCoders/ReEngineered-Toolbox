package xyz.brassgoggledcoders.reengineeredtoolbox.screen.socket;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.GuiTileAddonScreen;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.BlankFaceScreen;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class SocketScreen extends GuiTileAddonScreen implements IHasContainer<SocketContainer>, ISocketScreen {
    private final SocketContainer container;
    private final PlayerInventory playerInventory;
    private final IFaceScreen faceScreen;

    public SocketScreen(SocketContainer container, PlayerInventory playerInventory) {
        super(container.getSocketTileEntity(), IAssetProvider.DEFAULT_PROVIDER, true);
        this.container = container;
        this.faceScreen = Optional.ofNullable(container.getFaceInstance().getScreen(this))
                .orElseGet(BlankFaceScreen::new);
        this.playerInventory = playerInventory;
    }

    @Override
    public List<IFactory<IGuiAddon>> guiAddons() {
        return Lists.newArrayList();
    }

    @SuppressWarnings("unused")
    public static SocketScreen create(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        return new SocketScreen(container, playerInventory);
    }

    @Override
    @Nonnull
    public SocketContainer getContainer() {
        return container;
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}
