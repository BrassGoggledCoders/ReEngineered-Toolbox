package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient.FluidIngredient;

public record FreezerRecipe(
        ResourceLocation id,
        Ingredient itemInput,
        FluidIngredient fluidInput,
        ItemStack result,
        int time,
        int power
) implements Recipe<FreezerRecipeContainer> {
    @Override
    public boolean matches(@NotNull FreezerRecipeContainer pContainer, @NotNull Level pLevel) {
        return this.itemInput().test(pContainer.getItem(0)) && this.fluidInput.test(pContainer.getFluidInTank(0));
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull FreezerRecipeContainer pContainer) {
        return this.result().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return this.result();
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.id();
    }

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, this.itemInput);
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return ReEngineeredRecipes.FREEZER_SERIALIZER.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return ReEngineeredRecipes.FREEZER_TYPE.get();
    }
}
