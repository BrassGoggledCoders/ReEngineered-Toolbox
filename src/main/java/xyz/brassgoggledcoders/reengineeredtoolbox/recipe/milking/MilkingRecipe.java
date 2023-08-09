package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredRecipes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

public class MilkingRecipe implements Recipe<MilkingContainer> {
    private final ResourceLocation id;
    private final Either<EntityType<?>, TagKey<EntityType<?>>> input;
    private final Predicate<EntityType<?>> inputTest;
    private final FluidStack result;
    private final ItemStack resultStack;
    private final int coolDown;

    public MilkingRecipe(ResourceLocation id, Either<EntityType<?>, TagKey<EntityType<?>>> input, FluidStack result, int coolDown) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.coolDown = coolDown;
        this.inputTest = this.input.map(
                type -> entityType -> entityType == type,
                tagKey -> entityType -> entityType.is(tagKey)
        );
        this.resultStack = this.result.getFluid()
                .getFluidType()
                .getBucket(this.result);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(MilkingContainer pContainer, Level pLevel) {
        return this.inputTest.test(pContainer.getEntity().getType());
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull MilkingContainer pContainer) {
        return this.resultStack.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return this.resultStack;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return ReEngineeredRecipes.MILKING_SERIALIZER.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return ReEngineeredRecipes.MILKING_TYPE.get();
    }

    public Either<EntityType<?>, TagKey<EntityType<?>>> getInput() {
        return input;
    }

    public FluidStack getResult() {
        return this.result;
    }

    public int getCoolDown() {
        return this.coolDown;
    }
}
