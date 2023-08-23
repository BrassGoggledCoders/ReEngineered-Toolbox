package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.jsoning.RegistryJson;
import xyz.brassgoggledcoders.jsoning.StackJson;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;

import java.util.function.Consumer;

public class MilkingRecipeBuilder {
    private final FluidStack result;
    private Either<EntityType<?>, TagKey<EntityType<?>>> input;
    private int coolDown;

    public MilkingRecipeBuilder(FluidStack result) {
        this.result = result;
        this.input = null;
        this.coolDown = 600;
    }

    public MilkingRecipeBuilder withInput(EntityType<?> input) {
        this.input = Either.left(input);
        return this;
    }

    public MilkingRecipeBuilder withInput(TagKey<EntityType<?>> input) {
        this.input = Either.right(input);
        return this;
    }

    public MilkingRecipeBuilder withCoolDown(int coolDown) {
        this.coolDown = coolDown;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ResourceLocation fluidsKey = ForgeRegistries.FLUIDS.getKey(this.result.getFluid());
        if (fluidsKey != null) {
            if (fluidsKey.getNamespace().equals("minecraft")) {
                save(pFinishedRecipeConsumer, ReEngineeredToolbox.rl("milking/" + fluidsKey.getPath()));
            } else {
                save(pFinishedRecipeConsumer, new ResourceLocation(fluidsKey.getNamespace(), "milking/" + fluidsKey.getPath()));
            }
        } else {
            throw new IllegalStateException("Unregistered Item Detected as Result");
        }

    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        ensureValid();
        pFinishedRecipeConsumer.accept(new MilkingFinishedRecipe(pRecipeId, this.input, this.result, this.coolDown));
    }

    private void ensureValid() {
        if (this.result.isEmpty()) {
            throw new IllegalStateException("result cannot be empty");
        }
        if (this.input == null) {
            throw new IllegalStateException("input cannot be null");
        }
        if (coolDown <= 0) {
            throw new IllegalStateException("coolDown cannot be <= 0");
        }
    }

    public static MilkingRecipeBuilder of(Fluid fluid) {
        return new MilkingRecipeBuilder(new FluidStack(fluid, FluidType.BUCKET_VOLUME));
    }

    public static MilkingRecipeBuilder of(Fluid fluid, int amount) {
        return new MilkingRecipeBuilder(new FluidStack(fluid, amount));
    }

    public record MilkingFinishedRecipe(
            ResourceLocation recipeId,
            Either<EntityType<?>, TagKey<EntityType<?>>> input,
            FluidStack result,
            int coolDown
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            pJson.add("input", RegistryJson.writeValueOrTag(ForgeRegistries.ENTITY_TYPES, input));
            pJson.addProperty("coolDown", this.coolDown);
            pJson.add("result", StackJson.writeFluidStack(this.result));
        }

        @Override
        @NotNull
        public ResourceLocation getId() {
            return this.recipeId();
        }

        @Override
        @NotNull
        public RecipeSerializer<?> getType() {
            return ReEngineeredRecipes.MILKING_SERIALIZER.get();
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
