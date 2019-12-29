package xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.energy;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETObjects;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.util.OptionalMath;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.BiFunction;

public class EnergyConduitCore extends ConduitCore<OptionalInt, EnergyContext, EnergyConduitType> {
    public EnergyConduitCore() {
        super(RETObjects.ENERGY_TYPE.get(), RETObjects.ENERGY_CORE_TYPE.get());
    }

    @Nonnull
    @Override
    public OptionalInt request(EnergyContext energyContext) {
        return handleClients(ConduitClient::extractFrom, energyContext);
    }

    @Nonnull
    @Override
    public OptionalInt offer(EnergyContext energyContext) {
        return handleClients(ConduitClient::insertInto, energyContext);
    }

    private OptionalInt handleClients(BiFunction<ConduitClient<OptionalInt, EnergyContext, EnergyConduitType>,
            EnergyContext, OptionalInt> clientHandling, EnergyContext energyContext) {
        OptionalInt amountHandled = OptionalInt.empty();
        Iterator<ConduitClient<OptionalInt, EnergyContext, EnergyConduitType>> clientIterator = this.getClients().iterator();
        while (clientIterator.hasNext() && OptionalMath.lessThan(amountHandled, energyContext.getAmount())) {
            ConduitClient<OptionalInt, EnergyContext, EnergyConduitType> client = clientIterator.next();
            EnergyContext internalContext = new EnergyContext(OptionalMath.subtract(energyContext.getAmount(), amountHandled),
                    energyContext.isSimulate());
            amountHandled = OptionalMath.add(amountHandled, clientHandling.apply(client, internalContext));
        }
        return amountHandled;
    }
}
