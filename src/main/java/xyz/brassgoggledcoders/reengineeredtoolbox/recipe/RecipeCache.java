package xyz.brassgoggledcoders.reengineeredtoolbox.recipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public class RecipeCache<T extends Recipe<U>, U extends Container> implements INBTSerializable<StringTag> {
    private final RecipeType<T> recipeType;

    private ResourceLocation lastRecipeId;
    private T lastRecipe;

    private boolean tryGet = true;

    public RecipeCache(RecipeType<T> recipeType) {
        this.recipeType = recipeType;
    }

    public void reset() {
        tryGet = true;
    }

    public T getRecipe(U container, Level level) {
        if (this.lastRecipeId != null) {
            this.lastRecipe = level.getRecipeManager()
                    .getRecipeFor(recipeType, container, level, this.lastRecipeId)
                    .map(Pair::getSecond)
                    .orElse(null);
        }

        if (lastRecipe != null && !lastRecipe.matches(container, level)) {
            lastRecipe = null;
            tryGet = true;
        }

        if (lastRecipe == null && tryGet) {
            this.lastRecipe = level.getRecipeManager()
                    .getRecipeFor(recipeType, container, level)
                    .orElse(null);
            this.tryGet = false;
        }

        return lastRecipe;
    }


    @Override
    public StringTag serializeNBT() {
        if (this.lastRecipe != null) {
            return StringTag.valueOf(this.lastRecipe.getId().toString());
        }
        return StringTag.valueOf("");
    }

    @Override
    public void deserializeNBT(StringTag nbt) {
        this.lastRecipeId = ResourceLocation.tryParse(nbt.getAsString());
    }
}
