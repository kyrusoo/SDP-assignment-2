package sdp.assignment2;

import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FabricationService<F extends Family> {
    private final FabricationFactory<F> factory;
    private final Machine<F> machine;
    private final MaterialCatalog<F> materials;
    private final Map<UUID, FabricationJob<F>> submittedJobs = new HashMap<>();

    public FabricationService(FabricationFactory<F> factory) {
        this.factory = Objects.requireNonNull(factory, "Factory must not be null.");
        this.machine = Objects.requireNonNull(factory.createMachine(), "Machine must not be null.");
        this.materials = Objects.requireNonNull(factory.createMaterialCatalog(),
                "Material catalog must not be null.");
    }

    public JobQuote<F> quoteJob(Design design) {
        FabricationJob<F> job = factory.createJob(design);
        return new JobQuote<>(job, materials.quoteCost(job));
    }

    public UUID submitJob(Design design) {
        FabricationJob<F> job = factory.createJob(design);
        UUID id = UUID.randomUUID();
        materials.reserve(job);
        try {
            machine.enqueue(job);
        } catch (RuntimeException failure) {
            materials.release(job);
            throw failure;
        }
        submittedJobs.put(id, job);
        return id;
    }

    public boolean cancelJob(UUID id) {
        Objects.requireNonNull(id, "Job id must not be null.");
        FabricationJob<F> job = submittedJobs.get(id);
        if (job == null || !machine.remove(job)) {
            return false;
        }
        materials.release(job);
        submittedJobs.remove(id);
        return true;
    }

    public double availableStock() {
        return materials.availableStock();
    }

    public List<FabricationJob<F>> queuedJobs() {
        return List.copyOf(machine.queuedJobs());
    }
}
