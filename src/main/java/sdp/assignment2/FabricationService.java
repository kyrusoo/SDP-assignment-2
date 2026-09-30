package sdp.assignment2;

import java.util.List;
import java.util.Objects;

public final class FabricationService<F extends Family> {
    private final FabricationFactory<F> factory;
    private final Machine<F> machine;
    private final MaterialCatalog<F> materials;

    public FabricationService(FabricationFactory<F> factory) {
        this.factory = Objects.requireNonNull(factory, "Factory must not be null.");
        this.machine = Objects.requireNonNull(factory.createMachine(), "Machine must not be null.");
        this.materials = Objects.requireNonNull(factory.createMaterialCatalog(),
                "Material catalog must not be null.");
    }

    public FabricationJob<F> quoteJob(Design design) {
        return factory.createJob(design);
    }

    public double availableStock() {
        return materials.availableStock();
    }

    public List<FabricationJob<F>> queuedJobs() {
        return List.copyOf(machine.queuedJobs());
    }
}
