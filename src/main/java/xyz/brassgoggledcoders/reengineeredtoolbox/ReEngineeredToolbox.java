package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.Registrate;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETBlocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;

import javax.annotation.Nonnull;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineeredtoolbox";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private final static Lazy<AbstractRegistrate<?>> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .itemGroup(() -> new ItemGroup(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return RETBlocks.IRON_FRAME_BLOCK.asStack();
                }
            })
    );

    public ReEngineeredToolbox() {
        RETRegistries.setup();
        RETPanels.setup();
        RETBlocks.setup();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }

    public static AbstractRegistrate<?> getRegistrate() {
        return REGISTRATE.get();
    }
}
