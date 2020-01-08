package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import net.minecraft.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.FreezerRecipe;

public class Recipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
            new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, ReEngineeredToolbox.ID);

    public static final IRecipeType<FreezerRecipe> FREEZER_TYPE = IRecipeType.register(ReEngineeredToolbox.ID + ":freezer");
    public static final RegistryObject<GenericSerializer<FreezerRecipe>> FREEZER_SERIALIZER = RECIPE_SERIALIZERS.register("freezer",
            () -> new GenericSerializer<>(FREEZER_TYPE, FreezerRecipe.class));

    public static final IRecipeType<FurnaceRecipe> FURNACE_TYPE = IRecipeType.register(ReEngineeredToolbox.ID + ":furnace");
    public static final RegistryObject<FurnaceRecipeSerializer> FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("furnace",
            FurnaceRecipeSerializer::new);


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
