package xyz.brassgoggledcoders.reengineeredtoolbox.test.queue;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;

import java.util.Optional;

@RunWith(VoltzTestRunner.class)
public class TestItemStackQueue extends AbstractTest {
    @Test
    public void testQueueInsert() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();
        ItemStack cartStack = new ItemStack(Items.MINECART, 1, 0);

        assertEquals("Didn't accept the ItemStack", ItemStack.EMPTY, itemStackQueue.offer(cartStack));
    }

    @Test
    public void testQueuePull() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();
        ItemStack cartStack = new ItemStack(Items.MINECART, 1, 0);

        itemStackQueue.offer(cartStack);
        assert itemStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueMultipleInserts() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();

        itemStackQueue.offer(new ItemStack(Items.MINECART, 1, 0));
        itemStackQueue.offer(new ItemStack(Items.MINECART, 1, 0));

        assert itemStackQueue.pull().isPresent();
        assert itemStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueStackingInserts() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();

        itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 1, 0));
        itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 1, 0));

        assert itemStackQueue.pull().isPresent();
        assert !itemStackQueue.pull().isPresent();
    }

    @Test
    public void testQueueTooBigStackingInserts() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();

        itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 34, 0));
        itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 32, 0));

        assert itemStackQueue.pull().isPresent();

        Optional<ItemStack> secondItemStack = itemStackQueue.pull();
        assert secondItemStack.isPresent();
        assert secondItemStack.get().getCount() == 2;
    }

    @Test
    public void testQueueFullQueueStackingInserts() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();

        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 32, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 34, 0)).getCount() == 2;

        assert itemStackQueue.getLength() == 5;
    }

    @Test
    public void testFullQueueDifferentInserts() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();

        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT, 4, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 64, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT, 32, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT_SEEDS, 32, 0)).isEmpty();
        assert itemStackQueue.offer(new ItemStack(Items.WHEAT, 34, 0)).getCount() == 34;

        assert itemStackQueue.getLength() == 5;
    }

    @Test
    public void testQueueSimulatePull() {
        ItemStackQueue itemStackQueue = new ItemStackQueue();
        ItemStack cartStack = new ItemStack(Items.MINECART, 1, 0);

        itemStackQueue.offer(cartStack);
        assert itemStackQueue.peek().isPresent();
        assert itemStackQueue.pull().isPresent();
    }
}
