package net.minecraft.item.crafting;

public class FurnaceRecipeSerializer extends CookingRecipeSerializer<FurnaceRecipe> {
    public FurnaceRecipeSerializer() {
        super(FurnaceRecipe::new, 200);
    }
}
