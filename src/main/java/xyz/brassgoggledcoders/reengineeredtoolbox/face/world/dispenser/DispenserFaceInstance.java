package xyz.brassgoggledcoders.reengineeredtoolbox.face.world.dispenser;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone.RedstoneContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.FaceContainerBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.Slots;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class DispenserFaceInstance extends FaceInstance {
    private static final Random RANDOM = new Random();

    private final FakeDispenserBlockSource blockSource;
    private final PosInvHandler inventory;
    private final RedstoneConduitClient redstoneConduitClient;
    private boolean powered = false;

    public DispenserFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.inventory = new PosInvHandler("Dispenser", 71, 35, 4)
                .setOnSlotChanged((itemStack, slot) -> this.getSocket().markDirty())
                .setSlotPosition((index) -> Pair.of((index % 2) * 18, (index / 2) * 18));
        this.blockSource = new FakeDispenserBlockSource(socketContext);
        this.redstoneConduitClient = RedstoneConduitClient.createConsumer(this, this.getName());
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            this.openScreen(player);
            return true;
        } else {
            return super.onActivated(player, hand, hit);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("inventory", this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void onTick() {
        if (!this.getWorld().isRemote) {
            boolean currentPowerState = redstoneConduitClient.request(new RedstoneContext(true))
                    .orElse(0) > 0;
            if (currentPowerState != powered) {
                if (currentPowerState) {
                    int i = this.getDispenseSlot();
                    if (i < 0) {
                        this.getWorld().playEvent(1001, this.getSocket().getBlockPos(), 0);
                    } else {
                        ItemStack itemstack = inventory.getStackInSlot(i);
                        IDispenseItemBehavior idispenseitembehavior = DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.get(itemstack.getItem());
                        if (idispenseitembehavior != IDispenseItemBehavior.NOOP) {
                            inventory.setStackInSlot(i, idispenseitembehavior.dispense(blockSource, itemstack));
                        }

                    }
                }

                powered = currentPowerState;
            }
        }
    }

    public int getDispenseSlot() {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.inventory.getSlots(); ++k) {
            if (!this.inventory.getStackInSlot(k).isEmpty() && RANDOM.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    @Override
    @Nullable
    public IFaceContainer getContainer() {
        return new FaceContainerBuilder()
                .withSlots(Slots.getFromInventories(this.inventory))
                .finish();
    }

    @Override
    @Nullable
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this.inventory);
    }

    @Override
    public List<ConduitClient<?, ?, ?>> getConduitClients() {
        return Lists.newArrayList(redstoneConduitClient);
    }
}
