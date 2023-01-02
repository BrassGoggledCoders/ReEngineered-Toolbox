package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.blank.BlankTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.ItemTypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.redstone.RedstoneSlotType;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class TypedSlotTypes {
    public static final ResourceKey<Registry<TypedSlotType>> TYPED_SLOT_KEY = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("typed_slot_type", RegistryBuilder::new);

    private static final Supplier<ForgeRegistry<TypedSlotType>> TYPED_SLOT_REGISTRY = Suppliers.memoize(() ->
            RegistryManager.ACTIVE.getRegistry(TYPED_SLOT_KEY)
    );

    public static final RegistryEntry<TypedSlotType> ITEM = ReEngineeredToolbox.getRegistrate()
            .object("item")
            .simple(TYPED_SLOT_KEY, () -> new TypedSlotType(ItemTypedSlot::createForItem, ItemTypedSlot::new, () -> new ItemStack(Items.BUNDLE)));

    public static final RegistryEntry<TypedSlotType> REDSTONE = ReEngineeredToolbox.getRegistrate()
            .object("redstone")
            .simple(TYPED_SLOT_KEY, RedstoneSlotType::new);

    public static final RegistryEntry<TypedSlotType> BLANK = ReEngineeredToolbox.getRegistrate()
            .object("blank")
            .simple(TYPED_SLOT_KEY, () -> new TypedSlotType(typedSlotHolder -> null, BlankTypedSlot::new, () -> new ItemStack(Items.AIR)));


    public static ForgeRegistry<TypedSlotType> getRegistry() {
        return TYPED_SLOT_REGISTRY.get();
    }

    public static void setup() {

    }
}
