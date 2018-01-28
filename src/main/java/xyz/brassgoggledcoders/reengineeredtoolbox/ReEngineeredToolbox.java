package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.ItemCreativeFace;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.ItemFace;
import xyz.brassgoggledcoders.reengineeredtoolbox.proxy.IProxy;

@Mod(
        modid = ReEngineeredToolbox.MOD_ID,
        name = ReEngineeredToolbox.MOD_NAME,
        version = ReEngineeredToolbox.VERSION
)
public class ReEngineeredToolbox extends BaseModFoundation<ReEngineeredToolbox> {

    public static final String MOD_ID = "reengineeredtoolbox";
    public static final String MOD_NAME = "ReEngineered Toolbox";
    public static final String VERSION = "1.0.0";

    @Instance(MOD_ID)
    public static ReEngineeredToolbox INSTANCE;

    @SidedProxy(clientSide = "xyz.brassgoggledcoders.reengineeredtoolbox.proxy.ClientProxy",
            serverSide = "xyz.brassgoggledcoders.reengineeredtoolbox.proxy.ServerProxy")
    public static IProxy PROXY;


    public ReEngineeredToolbox() {
        super(MOD_ID, MOD_NAME, VERSION, new CreativeTabToolbox());
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        CapabilitySidedFaceHolder.register();
        CapabilityFaceHolder.register();
        PROXY.initModelLoader();
    }

    @Override
    public void registerBlocks(BlockRegistry registry) {
        registry.register(new BlockSocket());
    }

    @Override
    public void registerItems(ItemRegistry registry) {
        registry.register(new ItemFace());
        registry.register(new ItemCreativeFace());
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public ReEngineeredToolbox getInstance() {
        return INSTANCE;
    }
}
