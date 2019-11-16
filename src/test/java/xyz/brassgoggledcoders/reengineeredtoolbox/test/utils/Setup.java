package xyz.brassgoggledcoders.reengineeredtoolbox.test.utils;

import com.builtbroken.mc.testing.junit.capability.CapabilityUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.sided.SidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFaceHolder;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.ID;

public class Setup {
    public static void setupRegistries() {
        if (RETRegistries.FACES == null) {
            RegistryEventHandler.buildFaceRegistry(new RegistryEvent.NewRegistry());
            RegistryEventHandler.registerFaces(new RegistryEvent.Register<>(new ResourceLocation(ID, "faces"),
                    RETRegistries.FACES));
        }
    }

    public static void setupCaps() {
        if (CapabilityFaceHolder.FACE_HOLDER == null) {
            CapabilityUtils.setupCapability(IFaceHolder.class, nothing -> CapabilityFaceHolder.register(), tCapability ->
                    CapabilityFaceHolder.FACE_HOLDER = tCapability);

            SidedFaceHolder.emptyFace = RETRegistries.FACES.getValue(new ResourceLocation("reengineeredtoolbox:empty"));
        }
    }
}
