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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.ScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.BlankFaceScreen;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class SocketScreen extends GuiTileAddonScreen implements IHasContainer<SocketContainer>, ISocketScreen {
    private final SocketContainer container;
    private final ScreenBuilder screenBuilder;
    private final IFaceScreen faceScreen;

    public SocketScreen(SocketContainer container) {
        super(container.getSocketTileEntity(), IAssetProvider.DEFAULT_PROVIDER, true);
        this.container = container;
        this.screenBuilder = new ScreenBuilder();
        this.faceScreen = Optional.ofNullable(container.getFaceInstance().getScreen(this))
                .orElseGet(BlankFaceScreen::new);
        faceScreen.setup(this);
    }

    @Override
    public List<IFactory<IGuiAddon>> guiAddons() {
        return screenBuilder.getGuiAddonFactories();
    }

    public void renderBackground(int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(mouseX, mouseY, partialTicks);
        faceScreen.renderBackground(mouseX, mouseY, partialTicks);
    }

    public void renderForeground(int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(mouseX, mouseY, partialTicks);
        faceScreen.renderForeground(mouseX, mouseY, partialTicks);
    }

    @SuppressWarnings("unused")
    public static SocketScreen create(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        return new SocketScreen(container);
    }

    @Override
    @Nonnull
    public SocketContainer getContainer() {
        return container;
    }

    @Override
    public IScreenBuilder getScreenBuilder() {
        return screenBuilder;
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}
