package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.furnace;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface IRETCookingRecipe extends Recipe<RecipeWrapper> {

    int getPower();

    int getTime();
}
