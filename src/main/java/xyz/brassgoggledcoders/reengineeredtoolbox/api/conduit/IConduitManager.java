package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface IConduitManager extends INBTSerializable<CompoundNBT> {
    default <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            Supplier<? extends ConduitType<CONTENT, CONTEXT, TYPE>> conduitType) {
        return getCoresFor(conduitType.get());
    }

    default <CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>> Set<ConduitCore<CONTENT, CONTEXT, TYPE>> getCoresFor(
            @Nullable ConduitType<CONTENT, CONTEXT, TYPE> conduitType) {
        return Optional.ofNullable(conduitType)
                .map(value -> this.getAllCores().stream()
                        .map(value::cast)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet())
                )
                .orElseGet(Sets::newHashSet);
    }

    default boolean addCore(ConduitCore<?, ?, ?> conduitCore) {
        if (this.getAllCores().size() < this.getMaxCores()) {
            return this.getAllCores().add(conduitCore);
        } else {
            return false;
        }
    }

    int getMaxCores();

    Set<ConduitCore<?, ?, ?>> getAllCores();

    @Override
    default void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT conduitCores = nbt.getCompound("conduitCores");
        this.getAllCores().clear();
        for (String coreName : conduitCores.keySet()) {
            Optional.ofNullable(RETRegistries.CONDUIT_CORE_TYPES.getValue(new ResourceLocation(coreName)))
                    .ifPresent(conduitCoreType -> {
                        CompoundNBT conduitCoreNBT = conduitCores.getCompound(coreName);
                        ConduitCore<?, ?, ?> conduitCore = conduitCoreType.createCore();
                        conduitCore.setUuid(conduitCoreNBT.getUniqueId("uuid"));
                        conduitCore.deserializeNBT(conduitCoreNBT);
                        this.addCore(conduitCore);
                    });
        }

        if (this.getAllCores().isEmpty()) {
            RETObjects.REDSTONE_CORE_TYPE
                    .map(ConduitCoreType::createCore)
                    .ifPresent(this::addCore);
            RETObjects.ENERGY_CORE_TYPE
                    .map(ConduitCoreType::createCore)
                    .ifPresent(this::addCore);
        }

        RETObjects.EMPTY_TYPE.ifPresent(emptyConduitType -> {
            while (this.getAllCores().size() < this.getMaxCores()) {
                this.addCore(emptyConduitType.createEmptyCore());
            }
        });

    }

    @Override
    default CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        CompoundNBT conduitCoreNBT = new CompoundNBT();

        this.getAllCores().stream()
                .filter(conduitCore -> !conduitCore.isEmpty())
                .map(IConduitManager::serializerConduitCore)
                .forEach(pair -> conduitCoreNBT.put(pair.getKey().toString(), pair.getValue()));

        compoundNBT.put("conduitCores", conduitCoreNBT);
        return compoundNBT;
    }

    static Pair<ResourceLocation, CompoundNBT> serializerConduitCore(ConduitCore<?, ?, ?> conduitCore) {
        CompoundNBT compoundNBT = conduitCore.serializeNBT();
        compoundNBT.putUniqueId("uuid", conduitCore.getUuid());
        return Pair.of(conduitCore.getConduitCoreType().getRegistryName(), compoundNBT);
    }

    default Optional<ConduitCore<?, ?, ?>> getCoreByUUID(UUID uuid) {
        return this.getAllCores()
                .stream()
                .filter(conduitCore -> conduitCore.getUuid().equals(uuid))
                .findFirst();
    }
}
