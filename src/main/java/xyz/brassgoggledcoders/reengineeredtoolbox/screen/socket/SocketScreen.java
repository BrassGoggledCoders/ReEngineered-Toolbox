package xyz.brassgoggledcoders.reengineeredtoolbox.screen.socket;

import com.hrznstudio.titanium.client.gui.container.GuiContainerBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.ScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.BlankFaceScreen;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SocketScreen extends GuiContainerBase<SocketContainer> implements IHasContainer<SocketContainer>, ISocketScreen {
    private final SocketContainer container;
    private final ScreenBuilder screenBuilder;
    private final IFaceScreen faceScreen;

    public SocketScreen(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.container = container;
        this.screenBuilder = new ScreenBuilder();
        this.faceScreen = Optional.ofNullable(container.getFaceInstance().getScreen(this))
                .orElseGet(BlankFaceScreen::new);
        faceScreen.setup(this);
    }

    @SuppressWarnings("unused")
    public static SocketScreen create(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        return new SocketScreen(container, playerInventory, name);
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
