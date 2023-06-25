package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableObject;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;

import java.util.function.BiFunction;

public class CachedRecipe {
    public static <REC extends Recipe<INV>, INV extends Container> BiFunction<Level, INV, Option<REC>> cached(
            RecipeType<REC> recipeType
    ) {
        MutableObject<Option<REC>> recipeCache = new MutableObject<>(Option.empty());
        return (level, inventory) -> {
            Option<REC> recipe = recipeCache.getValue();
            if (!recipe.exists(value -> value.matches(inventory, level))) {
                recipe = Option.fromOptional(level.getRecipeManager()
                        .getRecipeFor(recipeType, inventory, level)
                );
                recipeCache.setValue(recipe);
            }
            return recipe;
        };
    }
}
