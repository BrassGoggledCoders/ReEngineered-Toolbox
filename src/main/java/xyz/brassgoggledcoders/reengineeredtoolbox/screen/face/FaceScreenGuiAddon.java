package xyz.brassgoggledcoders.reengineeredtoolbox.screen.face;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;

import java.util.List;

public class FaceScreenGuiAddon implements IFaceScreen, IGuiAddonProvider {
    private final IGuiAddonProvider guiAddonProvider;

    public FaceScreenGuiAddon(IGuiAddonProvider guiAddonProvider) {
        this.guiAddonProvider = guiAddonProvider;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return guiAddonProvider.getGuiAddons();
    }
}
