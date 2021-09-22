package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.RETRegistries;

import java.util.EnumMap;
import java.util.Map;

public class PanelModelShapes {
    private final Map<PanelState, EnumMap<Direction, IBakedModel>> modelByStateCache;
    private final PanelModelManager panelModelManager;

    public PanelModelShapes(PanelModelManager manager) {
        this.modelByStateCache = Maps.newIdentityHashMap();
        this.panelModelManager = manager;
    }

    public void clearCaches() {
        this.modelByStateCache.clear();
    }

    public IBakedModel getBakedModel(PanelState panelState, Direction direction) {
        return modelByStateCache.computeIfAbsent(panelState, value -> Maps.newEnumMap(Direction.class))
                .computeIfAbsent(direction, value -> Minecraft.getInstance().getModelManager().getMissingModel());
    }

    public void bakeModels(ModelBakery modelBakery) {
        for (Panel panel : RETRegistries.PANELS.get()) {
            for (PanelState panelState : panel.getStateContainer().getPossibleStates()) {
                for (Direction direction : Direction.values()) {
                    if (panelState.isValidFor(direction)) {
                        modelByStateCache.computeIfAbsent(panelState, value -> Maps.newEnumMap(Direction.class))
                                .put(direction, this.createBakedModel(modelBakery, panelState, direction));
                    }
                }
            }
        }
    }

    public IBakedModel createBakedModel(ModelBakery modelBakery, PanelState panelState, Direction direction) {
        IUnbakedModel model = this.panelModelManager.getUnbakedModel(stateToModelLocation(panelState));
        IModelTransform transform;
        switch (direction) {
            case EAST:
                transform = ModelRotation.X0_Y90;
                break;
            case WEST:
                transform = ModelRotation.X0_Y270;
                break;
            case DOWN:
                transform = ModelRotation.X90_Y0;
                break;
            case UP:
                transform = ModelRotation.X270_Y0;
                break;
            case SOUTH:
                transform = ModelRotation.X90_Y90;
                break;
            default:
                transform = ModelRotation.X0_Y0;
                break;
        }
        if (model != null) {
            return model.bake(
                    modelBakery,
                    modelBakery.getSpriteMap()::getSprite,
                    transform,
                    stateToModelLocation(panelState)
            );
        } else {
            return Minecraft.getInstance()
                    .getModelManager()
                    .getMissingModel();
        }
    }

    public static ModelResourceLocation stateToModelLocation(PanelState pState) {
        return stateToModelLocation(RETRegistries.PANELS.get().getKey(pState.getPanel()), pState);
    }

    public static ModelResourceLocation stateToModelLocation(ResourceLocation pLocation, PanelState pState) {
        return new ModelResourceLocation(pLocation, BlockModelShapes.statePropertiesToString(pState.getValues()));
    }
}
