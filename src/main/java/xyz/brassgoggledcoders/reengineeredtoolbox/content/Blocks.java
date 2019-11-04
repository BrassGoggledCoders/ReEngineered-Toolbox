package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.SocketBlock;

public class Blocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ReEngineeredToolbox.ID);

    public static final RegistryObject<Block> SOCKET = BLOCKS.register("socket", () -> new SocketBlock(Block.Properties.create(Material.IRON)
            .harvestLevel(2)
            .sound(SoundType.METAL)
            .hardnessAndResistance(5.0F, 6.0F)
    ));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
