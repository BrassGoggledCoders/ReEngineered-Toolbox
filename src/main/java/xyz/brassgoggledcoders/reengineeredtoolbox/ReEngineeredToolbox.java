package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.Conduits;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Faces;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Items;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.SocketModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.socket.SocketScreen;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineeredtoolbox";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static ReEngineeredToolbox instance;

    public final ItemGroup itemGroup = new TitaniumTab(ID, () -> new ItemStack(Blocks.SOCKET_ITEM.get()));

    public ReEngineeredToolbox() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        Faces.register(modBus);
        Blocks.register(modBus);
        Items.register(modBus);
        Recipes.register(modBus);
        Conduits.register(modBus);

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityFaceHolder.register();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new SocketModelLoader());
        ScreenManager.registerFactory(SocketContainer.type, SocketScreen::create);
    }
}
