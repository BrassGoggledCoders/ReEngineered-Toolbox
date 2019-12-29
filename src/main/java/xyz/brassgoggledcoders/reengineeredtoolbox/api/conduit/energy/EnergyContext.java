package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy;

public class EnergyContext {
    private final int amount;
    private final boolean simulate;

    public EnergyContext(int amount, boolean simulate) {
        this.amount = amount;
        this.simulate = simulate;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isSimulate() {
        return simulate;
    }
}
