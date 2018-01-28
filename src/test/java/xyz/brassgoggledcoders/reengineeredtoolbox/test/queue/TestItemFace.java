package xyz.brassgoggledcoders.reengineeredtoolbox.test.queue;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.capability.CapabilityUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.brassgoggledcoders.reengineeredtoolbox.RegistryEventHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.IFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.item.ItemFace;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

@RunWith(VoltzTestRunner.class)
public class TestItemFace extends AbstractTest {

    @Override
    @BeforeClass
    public void setUpForEntireClass() {
        RegistryEventHandler.buildFaceRegistry(new RegistryEvent.NewRegistry());
        RegistryEventHandler.registerFaces(new RegistryEvent.Register<>(new ResourceLocation(MOD_ID, "faces"),
                ToolboxRegistries.FACES));
        CapabilityUtils.setupCapability(IFaceHolder.class, nothing -> CapabilityFaceHolder.register(), tCapability ->
                CapabilityFaceHolder.FACE_HOLDER = tCapability);
    }

    @Test
    public void testCapabilityInit() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("face", new ResourceLocation(MOD_ID, "blank").toString());
        ItemStack itemStack = new ItemStack(new ItemFace(), 1, 0, tagCompound);

        IFaceHolder faceHolder = itemStack.getCapability(CapabilityFaceHolder.FACE_HOLDER, null);
        assert faceHolder != null;
        assert new ResourceLocation(MOD_ID, "blank").equals(faceHolder.getFace().getRegistryName());
    }
}
