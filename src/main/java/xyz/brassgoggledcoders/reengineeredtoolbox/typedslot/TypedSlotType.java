package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import com.google.common.base.Suppliers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TypedSlotType {
    private final Function<ITypedSlotHolder, ICapabilityProvider> createProvider;
    private final Supplier<? extends ITypedSlot<?>> defaultSlot;
    private final Supplier<ItemStack> display;
    private Object renderProperties;

    public TypedSlotType(Function<ITypedSlotHolder, ICapabilityProvider> createProvider, Supplier<? extends ITypedSlot<?>> defaultSlot, Supplier<ItemStack> display) {
        this.createProvider = createProvider;
        this.defaultSlot = defaultSlot;
        this.display = Suppliers.memoize(display::get);
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

    public ItemStack getDisplayStack() {
        return display.get();
    }
}
