package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public abstract class SideSelector<T> implements INBTSerializable<CompoundNBT> {
    private final SocketContext socketContext;
    private final SelectorType push;
    private final SelectorType pull;
    private final EnumMap<Direction, SelectorState> selectorStates;

    protected SideSelector(SocketContext socketContext, SelectorType push, SelectorType pull) {
        this.socketContext = socketContext;
        this.push = push;
        this.pull = pull;
        this.selectorStates = Maps.newEnumMap(Direction.class);
    }

    public void setSelectorState(Direction side, SelectorState selectorState) {
        selectorStates.put(side, selectorState);
    }

    public void tick(ISocketTile socketTile) {
        for (Entry<Direction, SelectorState> selectorStateEntry : selectorStates.entrySet()) {
            switch (selectorStateEntry.getValue()) {
                case PUSH:
                    if (push.isInternal()) {
                        activePush(socketTile, selectorStateEntry.getKey());
                    }
                    break;
                case PULL:
                    if (pull.isInternal()) {
                        activePull(socketTile, selectorStateEntry.getKey());
                    }
                    break;
                case NONE:
                    break;
            }
        }
    }

    public T getPassive(ISocketTile socketTile, SocketContext callerContext) {
        if (callerContext != null) {
            SelectorState state = this.getSelectorStates().get(callerContext.getSide());
            if (state != null) {
                if (state == SelectorState.PULL && this.getPull().isExternal()) {
                    return this.passivePull(socketTile, callerContext);
                } else if (state == SelectorState.PUSH && this.getPush().isExternal()) {
                    return this.passivePush(socketTile, callerContext);
                }
            }
        }
        return null;
    }

    public abstract T getValue(ISocketTile socketTile, boolean simulate);

    protected abstract void activePull(ISocketTile socketTile, Direction targetSide);

    protected abstract void activePush(ISocketTile socketTile, Direction targetSide);

    protected abstract T passivePull(ISocketTile socketTile, SocketContext callerSide);

    protected abstract T passivePush(ISocketTile socketTile, SocketContext callerSide);

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

    public Map<Direction, SelectorState> getSelectorStates() {
        return selectorStates;
    }

    protected SocketContext getSocketContext() {
        return socketContext;
    }
}
