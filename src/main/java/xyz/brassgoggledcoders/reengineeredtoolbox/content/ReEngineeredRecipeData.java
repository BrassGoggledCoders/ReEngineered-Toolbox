package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeMod;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipeBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient.FluidIngredient;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking.MilkingRecipeBuilder;

public class ReEngineeredRecipeData {
    public static void generateRecipes(RegistrateRecipeProvider provider) {
        generateFreezerRecipes(provider);
        generateMilkerRecipe(provider);
    }

    public static void generateFreezerRecipes(RegistrateRecipeProvider provider) {
        FreezerRecipeBuilder.of(Items.ICE)
                .withFluidInput(FluidIngredient.of(FluidTags.WATER))
                .save(provider, ReEngineeredToolbox.rl("freezer/ice"));
    }

    public static void generateMilkerRecipe(RegistrateRecipeProvider provider) {
        MilkingRecipeBuilder.of(ForgeMod.MILK.get())
                .withInput(EntityType.COW)
                .save(provider, ReEngineeredToolbox.rl("milking/milk"));

    }
}
