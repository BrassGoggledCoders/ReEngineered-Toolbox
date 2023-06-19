package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.FrameBlock;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameModelBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.render.FrameBlockEntityRenderer;

public class ReEngineeredBlocks {
    public static BlockEntry<FrameBlock> IRON_FRAME = ReEngineeredToolbox.getRegistrate()
            .object("iron_frame")
            .block(FrameBlock::new)
            .blockstate((context, provider) -> provider.simpleBlock(
                    context.get(),
                    provider.models()
                            .getBuilder(context.getName())
                            .customLoader(FrameModelBuilder::new)
                            .withFrame(provider.models()
                                    .nested()
                                    .renderType(provider.mcLoc("cutout"))
                                    .parent(provider.models()
                                            .getExistingFile(provider.modLoc("block/frame"))
                                    )
                                    .texture("frame", provider.blockTexture(context.get()))
                            )
                            .end()
            ))
            .item()
            .build()
            .register();

    public static BlockEntityEntry<FrameBlockEntity> IRON_FRAME_ENTITY = ReEngineeredToolbox.getRegistrate()
            .object("iron_frame")
            .blockEntity(FrameBlockEntity::new)
            .renderer(() -> FrameBlockEntityRenderer::new)
            .validBlock(IRON_FRAME)
            .register();

    public static void setup() {

    }
}
