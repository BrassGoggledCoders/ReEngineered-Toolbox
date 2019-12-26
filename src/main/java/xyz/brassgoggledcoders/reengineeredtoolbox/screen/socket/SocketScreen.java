package xyz.brassgoggledcoders.reengineeredtoolbox.screen.socket;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.client.gui.container.GuiContainerBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.addon.coreselector.ConduitCoreSelectorGuiAddon;
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
        FaceInstance faceInstance = container.getFaceInstance();
        this.faceScreen = Optional.ofNullable(faceInstance.getScreen())
                .orElseGet(BlankFaceScreen::new);
        faceScreen.setup(this);
        if (faceScreen instanceof IGuiAddonProvider) {
            ((IGuiAddonProvider) faceScreen).getGuiAddons()
                    .stream()
                    .map(IFactory::create)
                    .forEach(this.getAddons()::add);
        }
        this.screenBuilder.getGuiAddonFactories()
                .stream()
                .map(IFactory::create)
                .forEach(this.getAddons()::add);

        int conduitSelectX = 8;
        for (ConduitClient<?, ?, ?> conduitClient: faceInstance.getConduitClients()) {
            this.getAddons().add(new ConduitCoreSelectorGuiAddon<>(container.getSocket().getConduitManager(),
                    conduitClient, conduitSelectX, 80));
            conduitSelectX += 16;
        }
    }

    public static SocketScreen create(SocketContainer container, PlayerInventory playerInventory, ITextComponent name) {
        return new SocketScreen(container, playerInventory, name);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        faceScreen.renderBackground(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        faceScreen.renderForeground(mouseX, mouseY);
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
