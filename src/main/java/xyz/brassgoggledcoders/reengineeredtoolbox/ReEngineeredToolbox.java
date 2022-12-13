package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredBlocks;

import javax.annotation.Nonnull;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineered_toolbox";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private final static Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .creativeModeTab(() -> new CreativeModeTab(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return ReEngineeredBlocks.IRON_FRAME.asStack();
                }
            }, "ReEngineered Toolbox")
    );


    public ReEngineeredToolbox() {
        ReEngineeredBlocks.setup();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }
}
