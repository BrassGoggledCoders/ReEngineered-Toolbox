package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableObject;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;

public class CachedRecipe<REC extends Recipe<INV>, INV extends Container> {
    private final RecipeType<REC> recipeType;
    private final MutableObject<Option<REC>> recipeCache;

    public CachedRecipe(RecipeType<REC> recipeType) {
        this.recipeType = recipeType;
        this.recipeCache = new MutableObject<>(Option.empty());
    }

    public Option<REC> getRecipe() {
        return recipeCache.getValue();
    }

    public Option<REC> getRecipe(Level level, INV inventory) {
        Option<REC> recipe = recipeCache.getValue();
        if (!recipe.exists(value -> value.matches(inventory, level))) {
            recipe = Option.fromOptional(level.getRecipeManager()
                    .getRecipeFor(recipeType, inventory, level)
            );
            recipeCache.setValue(recipe);
        }
        return recipe;
    }
}
