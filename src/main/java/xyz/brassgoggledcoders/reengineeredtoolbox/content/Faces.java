package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemInputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemOutputFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.DispenserFaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.FurnaceFaceInstance;

public class Faces {
    private static final DeferredRegister<Face> FACES = new DeferredRegister<>(RETRegistries.FACES, ReEngineeredToolbox.ID);

    public static final RegistryObject<Face> BLANK = FACES.register("blank", Face::new);

    public static final RegistryObject<Face> FURNACE = FACES.register("furnace",
            () -> new Face(FurnaceFaceInstance::new));
    public static final RegistryObject<Face> DISPENSER = FACES.register("dispenser",
            () -> new Face(DispenserFaceInstance::new));

    public static final RegistryObject<Face> ITEM_INPUT = FACES.register("item_input",
            () -> new Face(ItemInputFaceInstance::new));
    public static final RegistryObject<Face> ITEM_OUTPUT = FACES.register("item_output",
            () -> new Face(ItemOutputFaceInstance::new));

    public static void register(IEventBus eventBus) {
        FACES.register(eventBus);
    }
}
