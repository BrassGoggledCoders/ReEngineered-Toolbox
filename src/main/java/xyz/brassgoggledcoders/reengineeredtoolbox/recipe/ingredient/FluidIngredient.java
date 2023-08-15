package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.JSONHelper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {
    public static final FluidIngredient EMPTY = new FluidIngredient(Either.left(Fluids.EMPTY), 0, null);

    private final Either<Fluid, TagKey<Fluid>> fluid;
    private final int amount;
    private final CompoundTag nbt;

    public FluidIngredient(Either<Fluid, TagKey<Fluid>> fluid, int amount, @Nullable CompoundTag nbt) {
        this.fluid = fluid;
        this.amount = amount;
        this.nbt = nbt;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean test(FluidStack fluidStack) {
        if (fluidStack.isEmpty() && this.isEmpty()) {
            return true;
        } else {
            boolean fluidMatches = fluid.map(
                    fluidStack.getFluid()::equals,
                    fluidStack.getFluid()::is
            );
            if (fluidMatches && fluidStack.getAmount() >= amount) {
                return nbt == null || nbt.equals(fluidStack.getTag());
            }
            return false;
        }
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public void toNetwork(FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(this.isEmpty());
        if (!this.isEmpty()) {
            byteBuf.writeBoolean(this.fluid.left().isPresent());
            this.fluid.ifLeft(value -> byteBuf.writeRegistryId(ForgeRegistries.FLUIDS, value));
            this.fluid.mapRight(TagKey::location)
                    .ifRight(byteBuf::writeResourceLocation);

            byteBuf.writeInt(this.amount);
            byteBuf.writeBoolean(this.nbt != null);
            if (this.nbt != null) {
                byteBuf.writeNbt(this.nbt);
            }
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fluid", String.valueOf(this.fluid.<String>map(
                value -> Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(value)).toString(),
                value -> "#" + value.location()
        )));
        jsonObject.addProperty("amount", this.amount);
        if (this.nbt != null) {
            jsonObject.addProperty("nbt", this.nbt.toString());
        }
        return jsonObject;
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf byteBuf) {
        if (byteBuf.readBoolean()) {
            return EMPTY;
        } else {
            Either<Fluid, TagKey<Fluid>> fluid;
            if (byteBuf.readBoolean()) {
                fluid = Optional.<Fluid>of(byteBuf.readRegistryId())
                        .filter(value -> value != Fluids.EMPTY)
                        .<Either<Fluid, TagKey<Fluid>>>map(Either::left)
                        .orElse(null);
            } else {
                fluid = Optional.ofNullable(ForgeRegistries.FLUIDS.tags())
                        .map(manager -> manager.createTagKey(byteBuf.readResourceLocation()))
                        .<Either<Fluid, TagKey<Fluid>>>map(Either::right)
                        .orElse(null);
            }
            if (fluid == null) {
                return EMPTY;
            } else {
                int amount = byteBuf.readInt();

                CompoundTag nbt = null;
                if (byteBuf.readBoolean()) {
                    nbt = byteBuf.readNbt();
                }

                return new FluidIngredient(
                        fluid,
                        amount,
                        nbt
                );
            }
        }
    }

    public static FluidIngredient fromJson(JsonObject jsonObject) throws JsonParseException {
        Either<Fluid, TagKey<Fluid>> fluid = JSONHelper.getValueOrTag(jsonObject, "fluid", ForgeRegistries.FLUIDS);
        int amount = GsonHelper.getAsInt(jsonObject, "amount", FluidType.BUCKET_VOLUME);
        if (amount <= 0) {
            throw new JsonParseException("'amount' must be greater than 0");
        }
        CompoundTag nbt = null;
        if (jsonObject.has("nbt")) {
            nbt = CraftingHelper.getNBT(jsonObject.get("nbt"));
        }
        return new FluidIngredient(fluid, amount, nbt);
    }


    public static FluidIngredient of(TagKey<Fluid> tagKey) {
        return new FluidIngredient(Either.right(tagKey), 1000, null);
    }
}
