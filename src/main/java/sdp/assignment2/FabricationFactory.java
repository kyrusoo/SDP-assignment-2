package sdp.assignment2;

public interface FabricationFactory<F extends Family> {
    FabricationJob<F> createJob(Design design);
    Machine<F> createMachine();
    MaterialCatalog<F> createMaterialCatalog();
}
