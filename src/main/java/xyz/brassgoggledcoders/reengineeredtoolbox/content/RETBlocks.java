package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.FrameBlock;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.FrameModelBuilder;

public class RETBlocks {
    public static final BlockEntry<FrameBlock> IRON_FRAME_BLOCK = ReEngineeredToolbox.getRegistrate()
            .object("iron_frame")
            .block(FrameBlock::new)
            .initialProperties(Material.METAL)
            .properties(AbstractBlock.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutout)
            .blockstate((context, provider) -> provider.simpleBlock(context.get(), provider.models()
                    .getBuilder("block/" + context.getId().getPath())
                    .customLoader(FrameModelBuilder::new)
                    .withFrame(new BlockModelBuilder(null, provider.models().existingFileHelper)
                            .parent(provider.models()
                                    .getExistingFile(provider.modLoc("block/frame"))
                            )
                            .texture("frame", ReEngineeredToolbox.rl("block/" + context.getId().getPath()))
                    )
                    .end()
            ))
            .item()
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<FrameBlockEntity>> FRAME_BLOCK_ENTITY =
            ReEngineeredToolbox.getRegistrate()
                    .object("frame")
                    .tileEntity(FrameBlockEntity::new)
                    .validBlocks(
                            IRON_FRAME_BLOCK
                    )
                    .register();

    public static void setup() {

    }
}
