package xyz.brassgoggledcoders.reengineeredtoolbox.api.sideselector;

import java.util.Optional;

public enum SelectorState {
    PUSH,
    PULL,
    NONE;

    private static final SelectorState[] VALUES = values();

    public static Optional<SelectorState> byName(String name) {
        for (SelectorState selectorState : VALUES) {
            if (selectorState.name().equals(name)) {
                return Optional.of(selectorState);
            }
        }
        return Optional.empty();
    }

    public SelectorState nextState(boolean allowPush, boolean allowPull) {
        SelectorState validNextState = null;
        do {
            SelectorState nextState = nextState();
            if (nextState == PUSH && allowPush) {
                validNextState = nextState;
            } else if (nextState == PULL && allowPull) {
                validNextState = nextState;
            } else if (nextState == NONE) {
                validNextState = nextState;
            }
        } while (validNextState == null);

        return validNextState;
    }

    public SelectorState nextState() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}
