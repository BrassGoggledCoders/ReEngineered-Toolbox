package xyz.brassgoggledcoders.reengineeredtoolbox.api.menu;

import net.minecraft.world.MenuProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;

public interface PanelMenuProvider extends MenuProvider {
    PanelPortInfo getPortInfo();

    PanelEntity getPanelEntity();
}
