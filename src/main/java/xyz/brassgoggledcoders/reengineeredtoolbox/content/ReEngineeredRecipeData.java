package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient.FluidIngredient;

public class ReEngineeredRecipeData {
    public static void generateRecipes(RegistrateRecipeProvider provider) {
        generateFreezerRecipes(provider);
    }

    public static void generateFreezerRecipes(RegistrateRecipeProvider provider) {
        FreezerRecipeBuilder.of(Items.ICE)
                .withFluidInput(FluidIngredient.of(FluidTags.WATER))
                .save(provider, ReEngineeredToolbox.rl("freezer/ice"));
    }
}
