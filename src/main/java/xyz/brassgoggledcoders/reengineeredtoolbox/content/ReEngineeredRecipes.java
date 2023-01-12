package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeSerializer;

public class ReEngineeredRecipes {
    public static final RegistryEntry<RecipeSerializer<FreezerRecipe>> FREEZER_SERIALIZER = ReEngineeredToolbox.getRegistrate()
            .object("freezer")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, FreezerRecipeSerializer::new);

    public static final RegistryEntry<RecipeType<FreezerRecipe>> FREEZER_TYPE = ReEngineeredToolbox.getRegistrate()
            .object("freezer")
            .simple(Registry.RECIPE_TYPE_REGISTRY, () -> RecipeType.simple(ReEngineeredToolbox.rl("freezer")));

    public static void setup() {

    }
}
