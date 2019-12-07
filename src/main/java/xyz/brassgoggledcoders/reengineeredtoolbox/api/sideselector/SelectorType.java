package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

public enum SelectorType {
    ALL(true, true),
    ACTIVE(true, false),
    PASSIVE(false, true),
    NONE(false, false);

    private final boolean internal;
    private final boolean external;

    SelectorType(boolean internal, boolean external) {
        this.internal = internal;
        this.external = external;
    }

    public boolean isInternal() {
        return internal;
    }

    public boolean isExternal() {
        return external;
    }
}
