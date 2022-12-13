package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredBlocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.registrate.ReEngineeredRegistrateAddon;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Mod(value = ReEngineeredToolbox.ID)
public class ReEngineeredToolbox {
    public static final String ID = "reengineered_toolbox";

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


    public ReEngineeredToolbox() {
        ReEngineeredBlocks.setup();
        ReEngineeredPanels.setup();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ReEngineeredRegistrateAddon<Registrate> getRegistrateAddon() {
        return REENGINEERED_REGISTRATE.get();
    }
}
