package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Faces;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.SocketModelLoader;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineeredtoolbox";
    public static ReEngineeredToolbox instance;

    public final ItemGroup itemGroup = new TitaniumTab(ID, () -> new ItemStack(Blocks.SOCKET_ITEM.get()));

    public ReEngineeredToolbox() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        Faces.register(modBus);
        Blocks.register(modBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new SocketModelLoader());
    }
}
