package xyz.brassgoggledcoders.reengineeredtoolbox.test.queue;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.FluidStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;

import java.util.Optional;

@RunWith(VoltzTestRunner.class)
public class TestFluidStackQueue extends AbstractTest {
    @Test
    public void testQueueInsert() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();

        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
    }

    @Test
    public void testQueuePull() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000));

        assert fluidStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueMultipleInserts() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000));
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000));

        assert fluidStackQueue.pull().isPresent();
        assert fluidStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueMultipleStackingInserts() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 500));
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 500));

        assert fluidStackQueue.pull().isPresent();
        assert !fluidStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueTooBigStackingInserts() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();

        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 500));
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 600));

        assert fluidStackQueue.pull().isPresent();

        Optional<FluidStack> fluidStack = fluidStackQueue.pull();
        assert fluidStack.isPresent();
        assert fluidStack.get().amount == 100;
    }

    @Test
    public void testFullQueueStackingInserts() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();

        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 500)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 550)).amount == 50;

        assert fluidStackQueue.getLength() == 5;
    }

    @Test
    public void testFluidQueueDifferentInserts() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();

        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.LAVA, 100)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.LAVA, 1)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 500)).amount == 0;
        assert fluidStackQueue.offer(new FluidStack(FluidRegistry.LAVA, 550)).amount == 550;

        assert fluidStackQueue.getLength() == 5;
    }

    @Test
    public void testQueueSimulatePull() {
        FluidStackQueue fluidStackQueue = new FluidStackQueue();
        fluidStackQueue.offer(new FluidStack(FluidRegistry.WATER, 1000));

        assert fluidStackQueue.peek().isPresent();
        assert fluidStackQueue.pull().isPresent();
    }
}
