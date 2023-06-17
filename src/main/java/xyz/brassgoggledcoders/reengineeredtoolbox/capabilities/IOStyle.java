package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities;

public enum IOStyle {
    BOTH(true, true),
    ONLY_INSERT(true, false),
    ONLY_EXTRACT(false, true);

    private final boolean allowInsert;
    private final boolean allowExtract;

    IOStyle(boolean allowInsert, boolean allowExtract) {
        this.allowInsert = allowInsert;
        this.allowExtract = allowExtract;
    }

    public boolean isAllowInsert() {
        return allowInsert;
    }

    public boolean isAllowExtract() {
        return allowExtract;
    }
}
