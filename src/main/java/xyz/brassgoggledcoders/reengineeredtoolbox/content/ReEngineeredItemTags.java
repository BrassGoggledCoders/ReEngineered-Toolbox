package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

public class ReEngineeredItemTags {
    public static final TagKey<Item> CAN_ALTER_FRAME_SLOT = ItemTags.create(ReEngineeredToolbox.rl("can_alter_frame_slot"));
    public static final TagKey<Item> CAN_REMOVE_PANEL = ItemTags.create(ReEngineeredToolbox.rl("can_remove_panel"));

    public static void generate(RegistrateTagsProvider<Item> tagsProvider) {
        tagsProvider.tag(CAN_ALTER_FRAME_SLOT)
                .addTag(Tags.Items.DYES);

        tagsProvider.tag(CAN_REMOVE_PANEL)
                .addOptionalTag(new ResourceLocation("forge", "tools/wrenches"))
                .addOptionalTag(new ResourceLocation("forge", "wrenches"))
                .addOptionalTag(new ResourceLocation("immersiveengineering", "hammer"));
    }

    public static void setup() {

    }
}
