package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FreezerRecipe extends SerializableRecipe {
    public ItemStack output = ItemStack.EMPTY;
    public Ingredient inputIngredient = Ingredient.EMPTY;
    public FluidStack inputFluidStack = FluidStack.EMPTY;
    public int time = 200;

    public FreezerRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        return inputFluidStack.isEmpty() && inputIngredient.test(inventory.getStackInSlot(0));
    }

    public boolean matches(ItemStack itemStack, FluidStack fluidStack) {
        return fluidStack.containsFluid(inputFluidStack) && inputIngredient.test(itemStack);
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public boolean canFit(int width, int height) {
        return inputFluidStack.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return Recipes.FREEZER_TYPE;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return Recipes.FREEZER_SERIALIZER.get();
    }
}
