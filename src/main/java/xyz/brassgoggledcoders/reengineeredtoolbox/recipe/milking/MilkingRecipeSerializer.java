package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.jsoning.RegistryJson;
import xyz.brassgoggledcoders.jsoning.StackJson;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class MilkingRecipeSerializer implements RecipeSerializer<MilkingRecipe> {
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public MilkingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        return new MilkingRecipe(
                pRecipeId,
                RegistryJson.valueOrTag(ForgeRegistries.ENTITY_TYPES, pSerializedRecipe, "input"),
                StackJson.readFluidStack(pSerializedRecipe, "result"),
                GsonHelper.getAsInt(pSerializedRecipe, "coolDown", 600)
        );
    }

    @Override
    @Nullable
    public MilkingRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new MilkingRecipe(
                pRecipeId,
                pBuffer.readEither(FriendlyByteBuf::readRegistryId, this::getTagKey),
                pBuffer.readFluidStack(),
                pBuffer.readInt()
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, MilkingRecipe pRecipe) {
        pBuffer.writeEither(
                pRecipe.getInput(),
                (friendlyByteBuf, entityType) -> friendlyByteBuf.writeRegistryId(ForgeRegistries.ENTITY_TYPES, entityType),
                (friendlyByteBuf, entityTypeTagKey) -> friendlyByteBuf.writeResourceLocation(entityTypeTagKey.location())
        );

        pBuffer.writeFluidStack(pRecipe.getResult());
        pBuffer.writeInt(pRecipe.getCoolDown());
    }

    public TagKey<EntityType<?>> getTagKey(FriendlyByteBuf friendlyByteBuf) {
        return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags())
                .createTagKey(friendlyByteBuf.readResourceLocation());
    }
}
