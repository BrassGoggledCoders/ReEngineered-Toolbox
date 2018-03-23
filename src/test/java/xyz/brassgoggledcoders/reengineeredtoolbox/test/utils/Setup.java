package xyz.brassgoggledcoders.reengineeredtoolbox.test.utils;

import com.builtbroken.mc.testing.junit.capability.CapabilityUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.RegistryEventHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.SidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.IFaceHolder;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class Setup {
    public static void setupRegistries() {
        if (ToolboxRegistries.FACES == null) {
            RegistryEventHandler.buildFaceRegistry(new RegistryEvent.NewRegistry());
            RegistryEventHandler.registerFaces(new RegistryEvent.Register<>(new ResourceLocation(MOD_ID, "faces"),
                    ToolboxRegistries.FACES));
        }
    }

    public static void setupCaps() {
        if (CapabilityFaceHolder.FACE_HOLDER == null) {
            CapabilityUtils.setupCapability(IFaceHolder.class, nothing -> CapabilityFaceHolder.register(), tCapability ->
                    CapabilityFaceHolder.FACE_HOLDER = tCapability);

            SidedFaceHolder.emptyFace = ToolboxRegistries.FACES.getValue(new ResourceLocation("reengineeredtoolbox:empty"));
        }
    }
}
