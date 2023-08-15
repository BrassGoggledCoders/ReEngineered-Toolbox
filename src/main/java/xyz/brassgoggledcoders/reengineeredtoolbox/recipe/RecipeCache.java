package xyz.brassgoggledcoders.reengineeredtoolbox.recipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;

import java.util.ArrayList;
import java.util.List;

public class RecipeCache<T extends Recipe<U>, U extends Container> implements INBTSerializable<StringTag> {
    private final RecipeType<T> recipeType;

    private ResourceLocation lastRecipeId;
    private T lastRecipe;
    private final List<T> savedChecks;
    private final int maxSavedChecks;

    private boolean tryGet = true;

    public RecipeCache(RecipeType<T> recipeType) {
        this(recipeType, 0);
    }

    public RecipeCache(RecipeType<T> recipeType, int savedChecks) {
        this.recipeType = recipeType;
        this.savedChecks = new ArrayList<>(savedChecks);
        this.maxSavedChecks = savedChecks;
    }

    public void reset() {
        tryGet = true;
    }

    public Option<T> getRecipe(U container, Level level) {
        if (this.lastRecipeId != null) {
            this.lastRecipe = level.getRecipeManager()
                    .getRecipeFor(recipeType, container, level, this.lastRecipeId)
                    .map(Pair::getSecond)
                    .orElse(null);
        }

        if (lastRecipe != null && !lastRecipe.matches(container, level)) {
            if (maxSavedChecks > 0 && !savedChecks.contains(lastRecipe)) {
                if (savedChecks.size() == maxSavedChecks) {
                    savedChecks.remove(0);
                }
                savedChecks.add(lastRecipe);
            }
            lastRecipe = null;
            tryGet = true;
        }

        if (lastRecipe == null && tryGet) {
            if (maxSavedChecks > 0) {
                for (T pastRecipe : savedChecks) {
                    if (pastRecipe.matches(container, level)) {
                        this.lastRecipe = pastRecipe;
                    }
                    if (this.lastRecipe != null) {
                        this.savedChecks.remove(this.lastRecipe);
                    }
                }
            }
            if (this.lastRecipe == null) {
                this.lastRecipe = level.getRecipeManager()
                        .getRecipeFor(recipeType, container, level)
                        .orElse(null);
            }

            this.tryGet = false;
        }

        return Option.ofNullable(lastRecipe);
    }

    public Option<T> getRecipe() {
        return Option.ofNullable(this.lastRecipe);
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
