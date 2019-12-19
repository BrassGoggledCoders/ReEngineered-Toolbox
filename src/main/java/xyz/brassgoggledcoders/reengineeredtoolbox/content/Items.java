package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.FaceItem;

public class Items {
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ReEngineeredToolbox.ID);

    public static final RegistryObject<Item> BLANK_FACE = ITEMS.register("blank_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.BLANK));
    public static final RegistryObject<Item> DISPENSER_FACE = ITEMS.register("dispenser_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.DISPENSER));
    public static final RegistryObject<Item> FURNACE_FACE = ITEMS.register("furnace_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.FURNACE));
    public static final RegistryObject<Item> FREEZER_FACE = ITEMS.register("freezer_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.FREEZER));

    public static final RegistryObject<Item> ITEM_INPUT_FACE = ITEMS.register("item_input_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.ITEM_INPUT));
    public static final RegistryObject<Item> ITEM_OUTPUT_FACE = ITEMS.register("item_output_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.ITEM_OUTPUT));

    public static final RegistryObject<Item> REDSTONE_INPUT_FACE = ITEMS.register("redstone_input_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.REDSTONE_INPUT));
    public static final RegistryObject<Item> REDSTONE_OUTPUT_FACE = ITEMS.register("redstone_output_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.REDSTONE_OUTPUT));

    public static final RegistryObject<Item> FLUID_INPUT_FACE = ITEMS.register("fluid_input_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.FLUID_INPUT));
    public static final RegistryObject<Item> FLUID_OUTPUT_FACE = ITEMS.register("fluid_output_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.FLUID_OUTPUT));

    public static final RegistryObject<Item> ENERGY_INPUT_FACE = ITEMS.register("energy_input_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.ENERGY_INPUT));
    public static final RegistryObject<Item> ENERGY_OUTPUT_FACE = ITEMS.register("energy_output_face",
            () -> new FaceItem(new Item.Properties().group(ReEngineeredToolbox.instance.itemGroup), Faces.ENERGY_OUTPUT));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
