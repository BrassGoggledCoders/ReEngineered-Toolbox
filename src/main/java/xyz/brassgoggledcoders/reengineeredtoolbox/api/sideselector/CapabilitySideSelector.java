package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CapabilitySideSelector<T> extends SideSelector<LazyOptional<T>> {
    private final Capability<T> capability;

    protected CapabilitySideSelector(Capability<T> capability, SocketContext socketContext, SelectorType push, SelectorType pull) {
        super(socketContext, push, pull);
        this.capability = capability;
    }

    @Override
    public void activePush(ISocketTile socketTile, Direction targetSide) {
        socketTile.getCapability(capability, targetSide, this.getSocketContext())
                .ifPresent(this::handlePush);
    }

    @Override
    public void activePull(ISocketTile socketTile, Direction targetSide) {
        socketTile.getCapability(capability, targetSide, this.getSocketContext())
                .ifPresent(this::handlePull);
    }

    @Override
    protected LazyOptional<T> passivePull(ISocketTile socketTile, SocketContext callerContext) {
        return this.getCapabilityForPull();
    }

    @Override
    protected LazyOptional<T> passivePush(ISocketTile socketTile, SocketContext callerContext) {
        return this.getCapabilityForPush();
    }

    @Nonnull
    public <U> LazyOptional<U> getCapability(ISocketTile socketTile, @Nonnull Capability<U> cap, @Nullable SocketContext socketContext) {
        if (cap == this.capability) {
            LazyOptional<T> optional = this.getPassive(socketTile, socketContext);
            if (optional != null) {
                return optional.cast();
            }
        }
        return LazyOptional.empty();
    }

    protected abstract void handlePull(T target);

    protected abstract void handlePush(T target);

    protected abstract LazyOptional<T> getCapabilityForPull();

    protected abstract LazyOptional<T> getCapabilityForPush();

}
