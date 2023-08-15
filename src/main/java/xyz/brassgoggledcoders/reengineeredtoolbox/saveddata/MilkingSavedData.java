package xyz.brassgoggledcoders.reengineeredtoolbox.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MilkingSavedData extends SavedData {
    private static final String NAME = ReEngineeredToolbox.rl("milking").toString();

    private final Map<UUID, Long> nextMilkingAllowed;

    public MilkingSavedData() {
        this.nextMilkingAllowed = new HashMap<>();
    }

    public void setNextMilkingAllowed(UUID entityId, long gameTime) {
        this.nextMilkingAllowed.put(entityId, gameTime);
    }

    public boolean isMilkingAllowed(UUID entityId, long gameTime) {
        return Option.ofNullable(this.nextMilkingAllowed.get(entityId))
                .exists(nextTime -> gameTime > nextTime);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        ListTag nextMilkingAllowListTag = new ListTag();
        for (Map.Entry<UUID, Long> entry: this.nextMilkingAllowed.entrySet()) {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("Id", entry.getKey());
            tag.putLong("Time", entry.getValue());

            nextMilkingAllowListTag.add(tag);
        }
        pCompoundTag.put("NextMilkingAllowed", nextMilkingAllowListTag);
        return pCompoundTag;
    }

    public static MilkingSavedData load(CompoundTag compoundTag) {
        MilkingSavedData milkingSavedData = new MilkingSavedData();

        ListTag nextMilkingAllowedListTag = compoundTag.getList("NextMilkingAllowed", Tag.TAG_COMPOUND);
        for (int i = 0; i < nextMilkingAllowedListTag.size(); i++) {
            CompoundTag nextMilkingAllowedTag = nextMilkingAllowedListTag.getCompound(i);
            milkingSavedData.setNextMilkingAllowed(
                    nextMilkingAllowedTag.getUUID("Id"),
                    nextMilkingAllowedTag.getLong("Time")
            );
        }

        return milkingSavedData;
    }

    public static Optional<MilkingSavedData> getFor(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return Optional.of(serverLevel.getDataStorage()
                    .computeIfAbsent(
                            MilkingSavedData::load,
                            MilkingSavedData::new,
                            NAME
                    )
            );
        }

        return Optional.empty();
    }
}
