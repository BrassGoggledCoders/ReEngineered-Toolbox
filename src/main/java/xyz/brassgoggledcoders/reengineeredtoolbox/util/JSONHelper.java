package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;
import java.util.Optional;

public class JSONHelper {


    public static <T> Either<T, TagKey<T>> getValueOrTag(JsonObject object, String fieldName, IForgeRegistry<T> registry) {
        String fieldValue = GsonHelper.getAsString(object, fieldName);
        if (fieldValue.startsWith("#")) {
            return Optional.ofNullable(ResourceLocation.tryParse(fieldValue.substring(1)))
                    .flatMap(name -> Optional.ofNullable(registry.tags())
                            .map(tagManager -> tagManager.createTagKey(name))
                    )
                    .<Either<T, TagKey<T>>>map(Either::right)
                    .orElseThrow(() -> new JsonParseException("'" + fieldName.substring(1) + "' is not a valid ResourceLocation"));
        } else {
            return Optional.ofNullable(ResourceLocation.tryParse(fieldValue))
                    .map(registry::getValue)
                    .<Either<T, TagKey<T>>>map(Either::left)
                    .orElseThrow(() -> new JsonParseException("'" + fieldName + "' is not a valid Registry value"));
        }
    }


    public static JsonElement writeItemStack(ItemStack result) {
        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem())).toString());
        if (result.getCount() > 1) {
            resultObject.addProperty("count", result.getCount());
        }
        if (result.getTag() != null) {
            resultObject.addProperty("nbt", result.getTag().toString());
        }
        return resultObject;
    }
}
