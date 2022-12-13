package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.FrameBlock;
import xyz.brassgoggledcoders.reengineeredtoolbox.blockentity.FrameBlockEntity;

public class ReEngineeredBlocks {
    public static BlockEntry<FrameBlock> IRON_FRAME = ReEngineeredToolbox.getRegistrate()
            .object("iron_frame")
            .block(FrameBlock::new)
            .blockstate((context, provider) -> provider.simpleBlock(
                    context.get(),
                    provider.models()
                            .withExistingParent(context.getName(), provider.modLoc("block/frame"))
                            .texture("frame", provider.blockTexture(context.get()))
            ))
            .item()
            .build()
            .register();

    public static BlockEntityEntry<FrameBlockEntity> IRON_FRAME_ENTITY = ReEngineeredToolbox.getRegistrate()
            .object("iron_frame")
            .blockEntity(FrameBlockEntity::new)
            .validBlock(IRON_FRAME)
            .register();

    public static void setup() {

    }
}
