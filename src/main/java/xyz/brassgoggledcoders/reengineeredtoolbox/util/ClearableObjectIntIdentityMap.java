package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.util.ObjectIntIdentityMap;

public class ClearableObjectIntIdentityMap<I> extends ObjectIntIdentityMap<I> {
    public void clear() {
        this.tToId.clear();
        this.idToT.clear();
        this.nextId = 0;
    }
}
