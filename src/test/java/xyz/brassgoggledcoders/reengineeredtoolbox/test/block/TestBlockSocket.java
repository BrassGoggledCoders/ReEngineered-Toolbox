package xyz.brassgoggledcoders.reengineeredtoolbox.test.block;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.test.utils.Setup;

@RunWith(VoltzTestRunner.class)
public class TestBlockSocket extends AbstractTest {
    private World world;

    @Override
    @BeforeClass
    public void setUpForEntireClass() {
        Setup.setupRegistries();
        Setup.setupCaps();

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
