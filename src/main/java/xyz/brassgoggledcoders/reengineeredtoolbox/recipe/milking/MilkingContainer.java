package xyz.brassgoggledcoders.reengineeredtoolbox.recipe.milking;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class MilkingContainer extends RecipeWrapper {
    private final Entity entity;

    public MilkingContainer(Entity entity) {
        super(new EmptyHandler());
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
