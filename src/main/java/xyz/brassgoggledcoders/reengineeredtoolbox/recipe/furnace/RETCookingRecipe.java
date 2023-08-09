package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.furnace;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class RETCookingRecipe implements IRETCookingRecipe {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack result;
    private final int time;
    private final int power;

    public RETCookingRecipe(ResourceLocation id, Ingredient input, ItemStack result, int time, int power) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.time = time;
        this.power = power;
    }


    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return this.input.test(pContainer.getItem(0));
    }

    @Override
    @NotNull
    public ItemStack assemble(RecipeWrapper pContainer) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.input);
        return nonnulllist;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getResult() {
        return result;
    }
}
