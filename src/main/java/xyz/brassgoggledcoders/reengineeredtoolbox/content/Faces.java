package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.core.EmptyFace;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.FaceInstanceItemInput;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.FaceInstanceItemOutput;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.FaceInstanceFurnace;

public class Faces {
    private static final DeferredRegister<Face> FACES = new DeferredRegister<>(RETRegistries.FACES, ReEngineeredToolbox.ID);

    public static final RegistryObject<Face> EMPTY = FACES.register("empty", EmptyFace::new);
    public static final RegistryObject<Face> BLANK = FACES.register("blank", Face::new);

    public static final RegistryObject<Face> FURNACE = FACES.register("furnace",
            () -> new Face(FaceInstanceFurnace::new));

    public static final RegistryObject<Face> ITEM_INPUT = FACES.register("item_input",
            () -> new Face(FaceInstanceItemInput::new));
    public static final RegistryObject<Face> ITEM_OUTPUT = FACES.register("item_output",

            () -> new Face(FaceInstanceItemOutput::new));

    public static void register(IEventBus eventBus) {
        FACES.register(eventBus);
    }
}
