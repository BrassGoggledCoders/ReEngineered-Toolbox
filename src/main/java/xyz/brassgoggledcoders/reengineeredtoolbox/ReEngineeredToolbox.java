package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.*;
import xyz.brassgoggledcoders.reengineeredtoolbox.network.NetworkHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.ReEngineeredRegistrateAddon;
import xyz.brassgoggledcoders.shadyskies.containersyncing.ContainerSyncing;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineered_toolbox";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    private final static Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID)
            .creativeModeTab(() -> new CreativeModeTab(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return ReEngineeredBlocks.IRON_FRAME.asStack();
                }
            }, "ReEngineered Toolbox")
    );

    private final static Supplier<ReEngineeredRegistrateAddon<Registrate>> REENGINEERED_REGISTRATE = Suppliers.memoize(() ->
            ReEngineeredRegistrateAddon.of(REGISTRATE.get())
    );

    private static ContainerSyncing containerSyncing;


    public ReEngineeredToolbox() {
        NetworkHandler.setup();

        ReEngineeredBlocks.setup();
        ReEngineeredMenus.setup();
        ReEngineeredPanels.setup();
        ReEngineeredRecipes.setup();
        ReEngineeredText.setup();

        containerSyncing = ContainerSyncing.setup(ID, LOGGER);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ReEngineeredRegistrateAddon<Registrate> getRegistrateAddon() {
        return REENGINEERED_REGISTRATE.get();
    }

    public static ContainerSyncing getSyncing() {
        return containerSyncing;
    }
}
