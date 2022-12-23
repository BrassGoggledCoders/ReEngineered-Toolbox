package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TypedSlotType {
    private final Function<ITypedSlotHolder, ICapabilityProvider> createProvider;
    private final Supplier<? extends ITypedSlot<?>> defaultSlot;
    private Object renderProperties;

    public TypedSlotType(Function<ITypedSlotHolder, ICapabilityProvider> createProvider, Supplier<? extends ITypedSlot<?>> defaultSlot) {
        this.createProvider = createProvider;
        this.defaultSlot = defaultSlot;
        this.initClient();
    }

    public ICapabilityProvider createProvider(ITypedSlotHolder typedSlotHolder) {
        return createProvider.apply(typedSlotHolder);
    }

    public ITypedSlot<?> createSlot() {
        return this.defaultSlot.get();
    }

    public void initializeClient(Consumer<ITypedSlotRenderProperties> consumer) {
    }

    private void initClient() {
        if (FMLEnvironment.dist.isClient() && !FMLLoader.getLaunchHandler().isData()) {
            initializeClient(properties -> this.renderProperties = properties);
        }
    }

    public Object getRenderProperties() {
        return renderProperties;
    }
}
