package xyz.brassgoggledcoders.reengineeredtoolbox.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;

import javax.annotation.Nonnull;
import java.util.Objects;

public class RETFurnaceRecipe extends AbstractCookingRecipe {
    public RETFurnaceRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(Recipes.FURNACE_TYPE, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return Objects.requireNonNull(Recipes.FURNACE_SERIALIZER.get());
    }
}
