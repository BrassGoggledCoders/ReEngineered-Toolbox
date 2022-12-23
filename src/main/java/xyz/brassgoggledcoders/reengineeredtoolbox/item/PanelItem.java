package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelLike;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import java.util.function.Supplier;

public class PanelItem<P extends Panel> extends Item implements PanelLike {
    private final Supplier<P> panelSupplier;

    public PanelItem(Supplier<P> panelSupplier, Properties pProperties) {
        super(pProperties);
        this.panelSupplier = panelSupplier;
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().getBlockEntity(pContext.getClickedPos()) instanceof IFrameEntity frameEntity) {
            PanelState panelState = this.asPanel().getPanelStateForPlacement(pContext, frameEntity);
            if (panelState != null) {
                InteractionResult setResult = frameEntity.putPanelState(pContext.getClickedFace(), panelState).getResult();
                if (setResult.consumesAction()) {
                    pContext.getItemInHand().shrink(1);
                }
                return setResult;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @NotNull
    public String getDescriptionId() {
        return this.asPanel().getDescriptionId();
    }

    @Override
    @NotNull
    public Panel asPanel() {
        return panelSupplier.get();
    }
}
