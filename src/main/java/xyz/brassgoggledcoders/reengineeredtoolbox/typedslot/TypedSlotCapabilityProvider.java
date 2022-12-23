package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TypedSlotCapabilityProvider<T> implements ICapabilityProvider {
    private final ITypedSlotHolder typedSlotHolder;
    private final Capability<T> capability;
    private final Function<ITypedSlotHolder, ? extends T> creator;
    private final LazyOptional<T> lazyOptional;

    public TypedSlotCapabilityProvider(ITypedSlotHolder typedSlotHolder, Capability<T> capability, Function<ITypedSlotHolder, ? extends T> creator) {
        this.typedSlotHolder = typedSlotHolder;
        this.capability = capability;
        this.creator = creator;
        this.lazyOptional = LazyOptional.of(this::create);
    }

    private T create() {
        return this.creator.apply(typedSlotHolder);
    }

    @NotNull
    @Override
    public <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, lazyOptional);
    }
}
