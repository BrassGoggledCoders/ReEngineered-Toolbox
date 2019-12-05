package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Optional;

public abstract class SideSelector<T> implements INBTSerializable<CompoundNBT> {
    private final SelectorType push;
    private final SelectorType pull;
    private final EnumMap<Direction, SelectorState> selectorStates;
    private final Capability<T> capability;

    protected SideSelector(Capability<T> capability, SelectorType push, SelectorType pull) {
        this.push = push;
        this.pull = pull;
        this.capability = capability;
        this.selectorStates = Maps.newEnumMap(Direction.class);
    }

    public void setSelectorState(Direction side, SelectorState selectorState) {
        selectorStates.put(side, selectorState);
    }

    public void tick(ISocketTile socketTile) {
        for (Entry<Direction, SelectorState> selectorStateEntry: selectorStates.entrySet()) {
            switch (selectorStateEntry.getValue()) {
                case PUSH:
                    socketTile.getInternalCapability(capability, selectorStateEntry.getKey())
                            .ifPresent(this::handlePush);
                    break;
                case PULL:
                    socketTile.getInternalCapability(capability, selectorStateEntry.getKey())
                            .ifPresent(this::handlePull);
                    break;
                case NONE:
                    break;
            }
        }
    }

    protected abstract void handlePull(T target);

    protected abstract void handlePush(T target);

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT fullNBT = new CompoundNBT();
        CompoundNBT selectorStatesNBT = new CompoundNBT();
        selectorStates.forEach((key, value) -> selectorStatesNBT.putString(key.getName(), value.name()));
        fullNBT.put("selectorStates", selectorStatesNBT);
        return fullNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT fullNBT) {
        CompoundNBT selectorStatesNBT = fullNBT.getCompound("selectorStates");
        selectorStatesNBT.keySet()
                .stream()
                .map(key -> Pair.of(key, selectorStatesNBT.getString(key)))
                .map(pair -> Optional.ofNullable(Direction.byName(pair.getFirst()))
                        .flatMap(direction -> SelectorState.byName(pair.getSecond())
                                .map(selectorState -> Pair.of(direction, selectorState))
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(pair -> selectorStates.put(pair.getFirst(), pair.getSecond()));
    }

    public SelectorType getPush() {
        return push;
    }

    public SelectorType getPull() {
        return pull;
    }
}
