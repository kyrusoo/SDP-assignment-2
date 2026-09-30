package sdp.assignment2;

public interface MaterialCatalog<F extends Family> {
    double availableStock();
    double quoteCost(FabricationJob<F> job);
    void reserve(FabricationJob<F> job);
    boolean release(FabricationJob<F> job);
}
