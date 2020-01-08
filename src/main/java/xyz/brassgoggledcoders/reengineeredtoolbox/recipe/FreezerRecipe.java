package xyz.brassgoggledcoders.reengineeredtoolbox.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Items;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class FreezerRecipe extends SerializableRecipe implements IMachineRecipe {
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
        return output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(Items.FREEZER_FACE.get());
    }

    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, inputIngredient);
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return Recipes.FREEZER_TYPE;
    }

    @Override
    @Nonnull
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return Objects.requireNonNull(Recipes.FREEZER_SERIALIZER.get());
    }

    @Override
    public int getTime() {
        return this.time;
    }
}
