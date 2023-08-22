package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.FacingPropertyComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NbtHelper;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class FrameItemOverrides extends ItemOverrides {
    private static final FramePanelKey ALL_PLUGS = new FramePanelKey(Arrays.stream(Direction.values())
            .sequential()
            .map(direction -> ReEngineeredPanels.PLUG.get()
                    .defaultPanelState()
            )
            .toArray(PanelState[]::new)
    );

    private final Cache<FramePanelKey, BakedModel> frameToModelCache;


    public FrameItemOverrides() {
        this.frameToModelCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.of(3, ChronoUnit.MINUTES))
                .build();
    }

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        if (!pStack.isEmpty()) {
            FramePanelKey panelKey = ALL_PLUGS;

            CompoundTag blockEntityTag = pStack.getTagElement("BlockEntityTag");
            if (blockEntityTag != null) {
                CompoundTag panelsTags = blockEntityTag.getCompound("Panels");

                if (!panelsTags.isEmpty()) {
                    PanelState[] panelStates = new PanelState[Direction.values().length];
                    for (Direction direction : Direction.values()) {
                        PanelState panelState = ReEngineeredPanels.PLUG.getDefaultState();
                        CompoundTag directionalPanelTag = panelsTags.getCompound(direction.getName());
                        if (!directionalPanelTag.isEmpty()) {
                            CompoundTag panelStateTag = directionalPanelTag.getCompound("PanelState");
                            if (!panelStateTag.isEmpty()) {
                                panelState = NbtHelper.readPanelState(panelStateTag);
                                FacingPropertyComponent component = panelState.getPanel().getComponent(FacingPropertyComponent.class);
                                if (component != null) {
                                    panelState = panelState.setValue(component.getProperty(), direction);
                                }
                            }
                        }
                        panelStates[direction.ordinal()] = panelState;
                    }
                    panelKey = new FramePanelKey(panelStates);
                }
            }

            BakedModel cachedModel = frameToModelCache.getIfPresent(panelKey);
            if (cachedModel == null) {
                cachedModel = new FrameItemBakedModel(panelKey, pModel);
                frameToModelCache.put(panelKey, cachedModel);
            }
            return cachedModel;
        }
        return pModel;
    }
}
