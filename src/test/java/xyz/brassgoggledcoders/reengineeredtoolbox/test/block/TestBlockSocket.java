package xyz.brassgoggledcoders.reengineeredtoolbox.test.block;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.brassgoggledcoders.reengineeredtoolbox.RegistryEventHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

@RunWith(VoltzTestRunner.class)
public class TestBlockSocket extends AbstractTest {
    World world;
    @Override
    @BeforeClass
    public void setUpForEntireClass() {
        RegistryEventHandler.buildFaceRegistry(new RegistryEvent.NewRegistry());
        RegistryEventHandler.registerFaces(new RegistryEvent.Register<>(new ResourceLocation(MOD_ID, "faces"),
                ToolboxRegistries.FACES));
        //CapabilityUtils.setupCapability(IFaceHolder.class, nothing -> CapabilityFaceHolder.register(), tCapability ->
        //        CapabilityFaceHolder.FACE_HOLDER = tCapability);
        //SidedFaceHolder.emptyFace = ToolboxRegistries.FACES.getValue(new ResourceLocation("reengineeredtoolbox:empty"));
        world = FakeWorld.newWorld("the_world");
    }

    @Test
    public void testPlaceBlock() {
        BlockPos socketPos = new BlockPos(8, 8, 8);
        BlockSocket socket = new BlockSocket();
        world.setBlockState(socketPos, socket.getDefaultState());

        TileEntity tileEntity = world.getTileEntity(socketPos);

        assert false;

        assert tileEntity != null;
        assert tileEntity.hasCapability(CapabilitySidedFaceHolder.SIDED_FACE_HOLDER, null);

    }
}
