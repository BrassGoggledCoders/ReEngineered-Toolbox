package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient.FluidIngredient;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.JSONHelper;

import java.util.function.Consumer;

public class FreezerRecipeBuilder {
    private final ItemStack result;
    private Ingredient itemInput;
    private FluidIngredient fluidInput;
    private int time;
    private int power;

    public FreezerRecipeBuilder(ItemStack result) {
        this.result = result;
        this.itemInput = Ingredient.EMPTY;
        this.fluidInput = FluidIngredient.EMPTY;
        this.time = 200;
    }

    public FreezerRecipeBuilder withItemInput(Ingredient itemInput) {
        this.itemInput = itemInput;
        return this;
    }

    public FreezerRecipeBuilder withFluidInput(FluidIngredient fluidInput) {
        this.fluidInput = fluidInput;
        return this;
    }

    public FreezerRecipeBuilder withTime(int time) {
        this.time = time;
        return this;
    }

    public FreezerRecipeBuilder withPower(int power) {
        this.power = power;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        if (itemId != null) {
            if (itemId.getNamespace().equals("minecraft")) {
                save(pFinishedRecipeConsumer, ReEngineeredToolbox.rl("freezer/" + itemId.getPath()));
            } else {
                save(pFinishedRecipeConsumer, new ResourceLocation(itemId.getNamespace(), "freezer/" + itemId.getPath()));
            }
        } else {
            throw new IllegalStateException("Unregistered Item Detected as Result");
        }

    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        ensureValid();
        pFinishedRecipeConsumer.accept(new FreezerFinishedRecipe(pRecipeId, this.itemInput, this.fluidInput, this.result, this.time));
    }

    private void ensureValid() {
        if (this.itemInput.isEmpty() && this.fluidInput.isEmpty()) {
            throw new IllegalStateException("Must include FluidInput or ItemInput");
        }
    }

    public static FreezerRecipeBuilder of(ItemLike itemLike) {
        return new FreezerRecipeBuilder(new ItemStack(itemLike, 1));
    }

    public static FreezerRecipeBuilder of(ItemLike itemLike, int count) {
        return new FreezerRecipeBuilder(new ItemStack(itemLike, count));
    }

    public record FreezerFinishedRecipe(
            ResourceLocation recipeId,
            Ingredient itemInput,
            FluidIngredient fluidInput,
            ItemStack result,
            int time
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.itemInput().isEmpty()) {
                pJson.add("itemInput", this.itemInput().toJson());
            }
            if (!this.fluidInput().isEmpty()) {
                pJson.add("fluidInput", this.fluidInput().toJson());
            }
            pJson.addProperty("time", this.time);
            pJson.add("result", JSONHelper.writeItemStack(this.result));
        }

        @Override
        @NotNull
        public ResourceLocation getId() {
            return this.recipeId();
        }

        @Override
        @NotNull
        public RecipeSerializer<?> getType() {
            return ReEngineeredRecipes.FREEZER_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
