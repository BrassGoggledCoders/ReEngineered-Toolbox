package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.redstone;

public class RedstoneContext {
    private final boolean strong;

    public RedstoneContext(boolean strong) {
        this.strong = strong;
    }

    public boolean isStrong() {
        return strong;
    }
}
