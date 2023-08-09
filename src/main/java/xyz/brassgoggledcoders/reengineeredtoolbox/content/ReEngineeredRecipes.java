package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeSerializer;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking.MilkingRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking.MilkingRecipeSerializer;

public class ReEngineeredRecipes {
    public static final RegistryEntry<RecipeSerializer<FreezerRecipe>> FREEZER_SERIALIZER = ReEngineeredToolbox.getRegistrate()
            .object("freezer")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, FreezerRecipeSerializer::new);

    public static final RegistryEntry<RecipeType<FreezerRecipe>> FREEZER_TYPE = ReEngineeredToolbox.getRegistrate()
            .object("freezer")
            .simple(Registry.RECIPE_TYPE_REGISTRY, () -> RecipeType.simple(ReEngineeredToolbox.rl("freezer")));

    public static final RegistryEntry<RecipeSerializer<MilkingRecipe>> MILKING_SERIALIZER = ReEngineeredToolbox.getRegistrate()
            .object("milking")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, MilkingRecipeSerializer::new);

    public static final RegistryEntry<RecipeType<MilkingRecipe>> MILKING_TYPE = ReEngineeredToolbox.getRegistrate()
            .object("milking")
            .simple(Registry.RECIPE_TYPE_REGISTRY, () -> RecipeType.simple(ReEngineeredToolbox.rl("freezer")));

    public static void setup() {

    }
}
