package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient.FluidIngredient;

public class FreezerRecipeSerializer implements RecipeSerializer<FreezerRecipe> {
    @Override
    @NotNull
    public FreezerRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject jsonObject) {
        if (!jsonObject.has("itemInput") && !jsonObject.has("fluidInput")) {
            throw new JsonParseException("Recipe must at least one of 'itemInput' or 'fluidInput'");
        }
        return new FreezerRecipe(
                pRecipeId,
                jsonObject.has("itemInput") ? CraftingHelper.getIngredient(jsonObject.get("itemInput")) : Ingredient.EMPTY,
                jsonObject.has("fluidInput") ? FluidIngredient.fromJson(jsonObject.getAsJsonObject("")) : FluidIngredient.EMPTY,
                CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true),
                GsonHelper.getAsInt(jsonObject, "time", 100)
        );
    }

    @Override
    @Nullable
    public FreezerRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        return new FreezerRecipe(
                pRecipeId,
                Ingredient.fromNetwork(pBuffer),
                FluidIngredient.fromNetwork(pBuffer),
                pBuffer.readItem(),
                pBuffer.readInt()
        );
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull FreezerRecipe pRecipe) {
        pRecipe.itemInput().toNetwork(pBuffer);
        pRecipe.fluidInput().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.result());
        pBuffer.writeInt(pRecipe.time());
    }
}
