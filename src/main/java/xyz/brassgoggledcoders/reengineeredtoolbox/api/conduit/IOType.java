package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit;

public enum IOType {
    BOTH(true, true),
    PUSH_ONLY(true, false),
    PULL_ONLY(false, true),
    NONE(false, false);

    private final boolean canPush;
    private final boolean canPull;

    IOType(boolean canPush, boolean canPull) {
        this.canPull = canPull;
        this.canPush = canPush;
    }

    public boolean canPull() {
        return canPull;
    }

    public boolean canPush() {
        return canPush;
    }
}
