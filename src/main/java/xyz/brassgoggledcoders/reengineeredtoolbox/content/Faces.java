package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy.EnergyInputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.energy.EnergyOutputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid.FluidInputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid.FluidOutputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemInputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemOutputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone.RedstoneInputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.redstone.RedstoneOutputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.FreezerFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.FurnaceFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.dispenser.DispenserFaceInstance;

public class Faces {
    private static final DeferredRegister<Face> FACES = new DeferredRegister<>(RETRegistries.FACES, ReEngineeredToolbox.ID);

    public static final RegistryObject<Face> BLANK = FACES.register("blank", Face::new);

    public static final RegistryObject<Face> FURNACE = FACES.register("furnace",
            () -> new Face(FurnaceFaceInstance::new));
    public static final RegistryObject<Face> FREEZER = FACES.register("freezer",
            () -> new Face(FreezerFaceInstance::new));
    public static final RegistryObject<Face> DISPENSER = FACES.register("dispenser",
            () -> new Face(DispenserFaceInstance::new));

    public static final RegistryObject<Face> ITEM_INPUT = FACES.register("item_input",
            () -> new Face(ItemInputFaceInstance::new));
    public static final RegistryObject<Face> ITEM_OUTPUT = FACES.register("item_output",
            () -> new Face(ItemOutputFaceInstance::new));

    public static final RegistryObject<Face> REDSTONE_INPUT = FACES.register("redstone_input",
            () -> new Face(RedstoneInputFaceInstance::new,
                    new ResourceLocation(ReEngineeredToolbox.ID, "faces/redstone_input_off")));
    public static final RegistryObject<Face> REDSTONE_OUTPUT = FACES.register("redstone_output",
            () -> new Face(RedstoneOutputFaceInstance::new,
                    new ResourceLocation(ReEngineeredToolbox.ID, "faces/redstone_output_off")));

    public static final RegistryObject<Face> FLUID_INPUT = FACES.register("fluid_input",
            () -> new Face(FluidInputFaceInstance::new));
    public static final RegistryObject<Face> FLUID_OUTPUT = FACES.register("fluid_output",
            () -> new Face(FluidOutputFaceInstance::new));

    public static final RegistryObject<Face> ENERGY_INPUT = FACES.register("energy_input",
            () -> new Face(EnergyInputFaceInstance::new));
    public static final RegistryObject<Face> ENERGY_OUTPUT = FACES.register("energy_output",
            () -> new Face(EnergyOutputFaceInstance::new));

    public static void register(IEventBus eventBus) {
        FACES.register(eventBus);
    }
}
