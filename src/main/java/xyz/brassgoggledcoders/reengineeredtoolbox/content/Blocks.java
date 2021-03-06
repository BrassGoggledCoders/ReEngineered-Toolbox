package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.SocketBlock;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.SocketTileEntity;

import java.util.Objects;

public class Blocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ReEngineeredToolbox.ID);
    public static final RegistryObject<Block> SOCKET = BLOCKS.register("socket", () -> new SocketBlock(Block.Properties.create(Material.IRON)
            .harvestLevel(2)
            .sound(SoundType.METAL)
            .hardnessAndResistance(5.0F, 6.0F)
    ));
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ReEngineeredToolbox.ID);
    public static final RegistryObject<Item> SOCKET_ITEM = ITEMS.register("socket",
            () -> new BlockItem(Objects.requireNonNull(SOCKET.get()), new Item.Properties()
                    .group(ReEngineeredToolbox.instance.itemGroup))
    );
    private static final DeferredRegister<TileEntityType<?>> TILE_TYPE =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ReEngineeredToolbox.ID);
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<?>> SOCKET_TYPE = TILE_TYPE.register("socket",
            () -> TileEntityType.Builder.create(SocketTileEntity::new, SOCKET.get()).build(null));
    private static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE =
            new DeferredRegister<>(ForgeRegistries.CONTAINERS, ReEngineeredToolbox.ID);
    public static final RegistryObject<ContainerType<?>> SOCKET_CONTAINER = CONTAINER_TYPE.register("socket",
            () -> IForgeContainerType.create(SocketContainer::create));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILE_TYPE.register(bus);
        CONTAINER_TYPE.register(bus);
    }
}
