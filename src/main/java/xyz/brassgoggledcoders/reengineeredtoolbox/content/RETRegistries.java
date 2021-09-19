package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class RETRegistries {

    public static final Supplier<IForgeRegistry<Panel>> PANELS = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("panel", Panel.class, RegistryBuilder::new);

    public static void setup() {

    }
}
