package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FreezerMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.FreezerScreen;

public class ReEngineeredMenus {

    public static MenuEntry<FreezerMenu> FREEZER = ReEngineeredToolbox.getRegistrate()
            .object("freezer")
            .menu(FreezerMenu::new, () -> FreezerScreen::new)
            .register();

    public static void setup() {

    }
}
