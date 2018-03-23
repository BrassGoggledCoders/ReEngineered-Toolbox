package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import java.util.List;

public class ItemCreativeFace extends ItemFace {

    public ItemCreativeFace() {
        super("creative_face");
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        return Lists.newArrayList(new ItemStack(this, 1, 0));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        Face currentFace = this.getFace(itemStack);
        ForgeRegistry<Face> faces = ToolboxRegistries.FACES;
        int id = faces.getID(currentFace);
        boolean foundFace = false;
        for (int testId = ++id; testId < id + 20; testId++) {
            Face nextFace = faces.getValue(testId);
            if (nextFace != null && !nextFace.equals(emptyFace)) {
                this.setFace(itemStack, nextFace);
                foundFace = true;
                break;
            }
        }

        if (!foundFace) {
            for (int testId = 0; testId < 10; testId++) {
                Face nextFace = faces.getValue(testId);
                if (nextFace != null && !nextFace.equals(emptyFace)) {
                    this.setFace(itemStack, nextFace);
                    foundFace = true;
                    break;
                }
            }
        }

        if (!foundFace) {
            player.sendMessage(new TextComponentString("Couldn't find Face"));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
