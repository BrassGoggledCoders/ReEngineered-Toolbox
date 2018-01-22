package com.brassgoggledcoders.reengineeredtoolbox;

import com.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.event.RegistryEvent;

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

    public ReEngineeredToolbox() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void registerBlocks(BlockRegistry registry) {
        registry.register(new BlockSocket());
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
